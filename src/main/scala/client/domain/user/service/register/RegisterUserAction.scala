package client.domain.user.service.register

import arch.common.Program.{ Context, MError, ProgramError }
import arch.infra.router.{ Action, RouterF }
import cats.implicits.*
import client.domain.user.UserRepoLive.UserRepoF
import client.domain.user.model.*
import client.domain.user.model.User.*
import client.domain.user.service.find.FindUserAction

case class RegisterUserAction(userId: String) extends Action[Either[ProgramError, Unit]]

object RegisterUserAction {
  private val ctx = Context("register-user-action")

  implicit def handler[
      F[_]: MError: UserRepoF: RouterF
  ]: RegisterUserAction => F[Either[ProgramError, Unit]] = { (action: RegisterUserAction) =>
    for {
      maybeUser <- RouterF[F].publish(
        FindUserAction(action.userId)
      )
      unit <- {
        maybeUser match {
          case Some(user) =>
            MError[F].raiseError(
              ProgramError(
                error = new Exception("user already exists"),
                ctx = ctx,
                msg = s"user already exists: $user"
              )
            )
          case None => UserRepoF[F].set(UserId(action.userId), User(action.userId))
        }
      }
    } yield Either.right[ProgramError, Unit](unit)
  }
}
