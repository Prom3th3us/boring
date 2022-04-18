package client.app

import arch.common.Program.MError
import arch.infra.repo.Repo
import arch.infra.router.{ Action, RouterF }
import client.app.Example6.language.*
import client.domain.user.UserRepoLive.{ UserRepoF, UserRepoTest }
import client.domain.user.model.User
import client.domain.user.model.User.*
import client.domain.user.service.find.FindUserAction
import client.infrastructure.ProgramLive.Test

object Example6 extends App {

  language
    .use(Some("intuition"))
    .log(println)
    .interpret

  object language:
    sealed trait Instruction[A]

    case class Log[A](log: State[A] => Unit)  extends Instruction[A]
    case class Find[A](log: State[A] => Unit) extends Instruction[A]

    case class State[A](data: Option[A])

    object State {
      def empty[A] = State[A](data = None)
    }

    def use[O](found: => Option[O]) = Carrier(List(), State.empty[O].copy(data = found))

    case class Carrier[A](
        instructions: List[Instruction[A]],
        state: State[A] = State.empty[A]
    ):

      def log(e: State[A] => Unit) = copy(instructions = instructions :+ Log[A](e))

      def find[F[_], r <: Repo[F]](using rr: r)(key: rr.Key): F[Option[rr.Value]] =
        rr.get(key)

      def interpret: Unit = interpreter.doIt[A](this)

  object interpreter:

    def find[F[_], r <: Repo[F]](using rr: r)(key: rr.Key): F[Option[rr.Value]] =
      rr.get(key)

    def set[F[_], r <: Repo[F]](using rr: r)(key: rr.Key, value: rr.Value): F[Unit] =
      rr.set(key, value)

    def doIt[A]: Carrier[A] => Unit = { carry =>
      import carry.*
      carry.instructions match { // use Fold instead of recursion
        case e :: rest =>
          e match {
            case e: Log[A] =>
              e.log(carry.state)
              doIt(Carrier(rest))
            case _ => ()

          }
        case Nil => ()
      }
    }
}
