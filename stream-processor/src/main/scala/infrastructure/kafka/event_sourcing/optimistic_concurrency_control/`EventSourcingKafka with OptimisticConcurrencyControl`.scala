package infrastructure.kafka.event_sourcing.optimistic_concurrency_control

import domain.{ Rules, State }
import infrastructure.kafka.event_sourcing.core.`EventSourcingKafka core`
import infrastructure.kafka.optimistic_concurrency_control.OptimisticConcurrencyControl
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.streams.scala._
import org.apache.kafka.streams.scala.kstream._

protected[event_sourcing] class `EventSourcingKafka with OptimisticConcurrencyControl`[
    Key,
    Command <: OptimisticConcurrencyControl,
    Event,
    S <: State[Event, S] with OptimisticConcurrencyControl,
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
    eventsSerde: Serde[Seq[Event]],
    stateSerde: Serde[S],
    builder: StreamsBuilder
) extends `EventSourcingKafka core`[Key, Command, Event, S, Error, R](
      name,
      initialState,
      rules
    ) {

  override lazy val commands: KStream[Key, Command] =
    OptimisticConcurrencyControl(
      commandsTopic,
      builder.stream(commandsTopic)(
        Consumed
          .`with`[Key, Command]
          .withName(s"$commandsTopic.in")
      ),
      snapshots
    )

}
