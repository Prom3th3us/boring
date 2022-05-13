package infrastructure.kafka.optimistic_concurrency_control
import play.api.libs.json.{Format, Json}

object HasOptimisticConcurrencyControl {

  case class Command[A](
      payload: A,
      expectedVersion: Long
  ) extends OptimisticConcurrencyControl

  case class State[
      E,
      S <: domain.State[E, S]
  ](
      state: S,
      expectedVersion: Long
  ) extends domain.State[E, State[E, S]]
      with OptimisticConcurrencyControl {
    override def +(event: E): State[E, S] = {
      copy(
        expectedVersion = this.expectedVersion + 1,
        state = state + event
      )
    }
  }

  object Command {
    implicit def format[E: Format]: Format[Command[E]] = Json.format
  }

  object State {

    implicit def format[E: Format, S <: domain.State[E, S]: Format]
        : Format[State[E, S]] = Json.format

  }
}
