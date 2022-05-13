package infrastructure.kafka.optimistic_concurrency_control

import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.streams.scala.kstream.{ Joined, KStream, KTable, Produced }

trait OptimisticConcurrencyControl {
  def expectedVersion: Long
}
object OptimisticConcurrencyControl {
  def apply[
      Key,
      A <: OptimisticConcurrencyControl,
      B <: OptimisticConcurrencyControl
  ](topic: String, stream: KStream[Key, A], table: KTable[Key, B])(implicit
      serdeKey: Serde[Key],
      serdeA: Serde[A],
      serdeB: Serde[B]
  ): KStream[Key, A] = {

    import infrastructure.kafka.utils.KafkaStreamsUtils.KStreamEitherUtils
    val (retry, proceed) = stream
      .leftJoin(table) { (command, state) =>
        println(s"Reading from state ${command} ${state}")
        Option(state) match {
          case Some(state) if command.expectedVersion > state.expectedVersion =>
            println(
              s"""
                 |Expected version difference, rejecting:
                 | command.expectedVersion: ${command.expectedVersion}
                 | state.expectedVersion: ${state.expectedVersion}
                 | -- command: ${command}
                 | -- state:${state}
                 |The version of the state is below expected. This means that there is a change incoming that we need to wait for.
                 |""".stripMargin
            )
            Left apply command
          case Some(state) if command.expectedVersion < state.expectedVersion =>
            println(
              s"""
                 |Expected version difference:
                 | command.expectedVersion: ${command.expectedVersion} 
                 | state.expectedVersion: ${state.expectedVersion} 
                 | -- command: ${command} 
                 | -- state:${state}
                 |The version of the state is more than expected. I dont know what this means. 
                 |But I think if I apply this command it will be applied too many times!
                 |""".stripMargin
            )
            Right apply command
          case _ => Right apply command
        }
      }(
        Joined
          .`with`[Key, A, B]
          .withName(s"$topic-join-${table.queryableStoreName}")
      )
      .splitEither("optimisticConcurrencyControl")(
        "retry",
        "proceed"
      )

    retry.to(topic)(Produced.`with`[Key, A].withName(s"$topic-retry.out"))
    proceed

  }

}
