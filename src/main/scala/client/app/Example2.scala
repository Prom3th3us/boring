package client.app

import arch.common.Program.MError
import arch.infra.router.{ Action, RouterF }
import client.domain.user.service.find.FindUserAction

object Example2 /* extends App {

  object language:
    case class State[A](data: Option[A])
    object State {
      def empty[A] = State[A](data = None)
    }

    case class Baggage[A](state: State[A] = State.empty[A])

    def find[O](found: => Option[O]) = Baggage(State.empty[O].copy(data = found))

    implicit class BaggageLog[In](baggage: Baggage[In]) {
      def log(io: In => Unit) = {
        baggage.state.data.foreach(io)
        baggage
      }
    }
    implicit class BaggageFilter[In](baggage: Baggage[In]) {
      def filter(io: In => Boolean) = Baggage[In](State(baggage.state.data.filter(io)))
    }
    implicit class BaggageMap[In, Out](baggage: Baggage[In]) {
      def map(io: In => Out) = Baggage[Out](State(baggage.state.data.map(io)))
    }

  val aa: Option[Int] =
    language
      .find { Some("user A") }
      .filter { _ => true }
      .log(e => println(s"HERE ${e}"))
      .map(_ => 10)
      .map(e => e * e)
      .log(e => println(s"HERE ${e}"))
      .state
      .data

  val bb =
    language
      .find { Some(FindUserAction("pepe")) }
      .filter { _ => true }
      .log(e => println(s"HERE ${e}"))
      .map(_ => 10)
      .map(e => e * e)
      .log(e => println(s"HERE ${e}"))
      .state

}
 */
