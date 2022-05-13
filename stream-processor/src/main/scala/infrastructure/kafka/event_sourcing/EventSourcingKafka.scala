package infrastructure.kafka.event_sourcing

import domain.{ Rules, State }
import infrastructure.kafka.optimistic_concurrency_control.HasOptimisticConcurrencyControl.{
  Command => EnrichedCommand,
  State => EnrichedState
}
import infrastructure.kafka.utils.SerdeUtils.serde
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.streams.kstream.Named
import org.apache.kafka.streams.scala._
import org.apache.kafka.streams.scala.kstream._
import org.apache.kafka.streams.scala.serialization.Serdes._
import play.api.libs.json.Format
class EventSourcingKafka[
    Key,
    Command: Format,
    Event: Format,
    S <: State[Event, S]: Format,
    Error,
    R <: Rules[Event, S, Command, Error]
](
    name: String,
    initialState: S,
    rules: R
)(implicit
    keySerde: Serde[Key],
    commandSerde: Serde[Command],
    eventSerde: Serde[Event],
    stateSerde: Serde[S],
    builder: StreamsBuilder
) {

  lazy val commandsTopic: String = s"${name}-commands"

  def process: Unit = {

    val commandStream: KStream[Key, Command] =
      builder.stream[Key, Command](commandsTopic)(
        Consumed.`with`[Key, Command].withName(s"$commandsTopic.in")
      )

    val commandIndexesTable: KTable[Key, Long] =
      commandStream
        .groupByKey(
          Grouped
            .`with`[Key, Command]
            .withName(s"$commandsTopic.groupedByKey")
        )
        .count(Named.as(s"$commandsTopic.count"))(
          Materialized.as(s"$commandsTopic.count.store")
        )

    val zippedWithIndexTopic = s"$commandsTopic-zippedWithIndex"
    commandStream
      .join(commandIndexesTable) { (c, l) =>
        println(s"Enriched command to index ${l} -- $c")
        EnrichedCommand(c, l)
      }(
        Joined
          .`with`[Key, Command, Long]
          .withName(
            s"$commandsTopic" + "-join-" + s"$commandsTopic.count.store"
          )
      )
      .to(zippedWithIndexTopic)(
        Produced
          .`with`[Key, infrastructure.kafka.optimistic_concurrency_control.HasOptimisticConcurrencyControl.Command[
            Command
          ]]
          .withName(s"$zippedWithIndexTopic.out")
      )

    new optimistic_concurrency_control.`EventSourcingKafka with OptimisticConcurrencyControl`[
      Key,
      EnrichedCommand[Command],
      Event,
      EnrichedState[Event, S],
      Error,
      Rules[Event, EnrichedState[Event, S], EnrichedCommand[Command], Error]
    ](
      name,
      EnrichedState(initialState, 1),
      context =>
        command => {
          rules.validator(context.state)(command.payload)
        }
    ) {
      override def publishSnapshots: Unit = {
        snapshots
          .toStream(Named.as(s"$snapshotsTopic.toStream"))
          .map(
            { (k, v) =>
              (k, v.state)
            },
            Named.as(s"$snapshotsTopic.simplify")
          )
          .to(s"$snapshotsTopic")(
            Produced.`with`[Key, S].withName(s"$snapshotsTopic.out")
          )
      }
      override lazy val commandsTopic: String = zippedWithIndexTopic
    }.process

  }

}
