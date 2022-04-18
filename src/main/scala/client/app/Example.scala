package client.app

import arch.infra.router.{ Action, RouterF }
import arch.common.Program.MError
import client.app.Example.language.interpreter

object Example extends App {

  interpreter.doIt(
    language
      .find { Some("user A") }
      .when { _ => true }
      .log(e => println(s"HERE ${e}"))
      .map(_ => 10)
      .map(e => e * e)
      .log(e => println(s"HERE ${e}"))
  )

  case object language:

    // ---
    case class State[A](data: Option[A])
    object State {
      def empty[A] = State[A](data = None)
    }

    sealed trait Sintax[A]

    // def map[Out](operation: A => MapOut) = state.data.map(s => Baggage[Out](
    //  List(), // TODO careful
    //  State(Some(operation(s))))
    // )

    case class When[A](predicate: A => Boolean) extends Sintax[A]
    case class Log[A](log: State[A] => Unit)    extends Sintax[A]
    case class Map[A, Out](operation: A => Out) extends Sintax[A]

    case class Baggage[A](lines: List[Sintax[A]], state: State[A] = State.empty[A]) {

      // case class Find[Out](a: Option[Out])     extends Sintax[A]

      def when(predicate: A => Boolean) = copy(lines = lines :+ When[A](predicate))
      def log(e: State[A] => Unit)      = copy(lines = lines :+ Log[A](e))

      // map[O] { old => f }}
      // Find
      // Search
      // Delete
      // Create
    }

    // def when(predicate:  => Boolean) = Baggage(List(When(predicate)))
    def find[O](found: => Option[O]) = Baggage(List(), State.empty[O].copy(data = found))

    implicit class BaggageMap[In, Out](baggage: Baggage[In]) {
      def map(io: In => Out) = Baggage[Out](List(), State(baggage.state.data.map(io)))
    }

    case class ExampleRule(id: String) extends Action[Boolean]
    object ExampleRule {
      implicit def handler[F[_]: MError]: ExampleRule => F[Boolean] = { (user: ExampleRule) =>
        MError[F].pure(true)
      }
    }
    case class FindUser(name: String) extends Action[String]
    object FindUser {
      implicit def handler[F[_]: MError]: FindUser => F[String] = { (user: FindUser) =>
        MError[F].pure(s"Found ${user.name}.")
      }
    }
    case class AddSalutation(text: String) extends Action[String]
    object SaluteUser {
      implicit def handler[F[_]: MError]: AddSalutation => F[String] = { (in: AddSalutation) =>
        MError[F].pure(s"${in.text} I salute you.")
      }
    }
    case class Print(text: String) extends Action[Unit]
    object Print {
      implicit def handler[F[_]: MError]: Print => F[Unit] = { (in: Print) =>
        MError[F].pure {
          println(in.text)
        }
      }
    }

    // .when(Unit => true)
    // .log[String](e => print(e))
    // .map(text => s"${text} I salute you.")

    object interpreter:
      def doIt[A]: Baggage[A] => Unit = { baggage =>
        import baggage._
        baggage.lines match { // use Fold instead of recursion
          case e :: rest =>
            e match {
              case e: When[A] =>
                baggage.state.data.map { state =>
                  if (e.predicate(baggage.state.data.get)) doIt(Baggage(rest, baggage.state))
                }
              case e: Log[A] =>
                e.log(baggage.state)
                doIt(Baggage(rest))
              // e() -- goes to unit, side effectfull, like printing or logging
              // case Map(operation) =>
              //  baggage.state.data.map(operation)
              //  doIt(Baggage(rest))
              // case Find(found) =>
              //   doIt(Baggage(rest, State(found))) // use a Map to store state

              case other => println(s"No other case... ${other}")
            }
          case Nil => ()
        }

      }

}
