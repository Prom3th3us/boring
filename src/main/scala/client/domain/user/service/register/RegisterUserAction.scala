package client.domain.user.service.register

import arch.common.Program.{ Context, MError, ProgramError }
import arch.infra.router.{ Action, RouterF }
import cats.implicits.*
import client.domain.user.UserRepoLive.UserRepoF
import client.domain.user.model.*
import client.domain.user.model.User.*
import client.domain.user.service.find.FindUserAction
import arch.infra.repo.Repo._
import arch.infra.router.Router._
import arch.common.Program.MError._
case class RegisterUserAction(userId: UserId, user: User) extends Action[Either[ProgramError, Unit]]

object RegisterUserAction {
  private val ctx = Context("register-user-action")

  implicit def handler[
      F[_]: MError: UserRepoF: RouterF
  ]: RegisterUserAction => F[Either[ProgramError, Unit]] = { (action: RegisterUserAction) =>
    for {
      maybeUser: Option[User] <- publish(FindUserAction(action.userId))
      unit: Unit <- {
        maybeUser match {
          case Some(user) =>
            MError[F].raiseError(
              ProgramError(
                error = new Exception("user already exists"),
                ctx = ctx,
                msg = s"user already exists: $user"
              )
            )
          case None => set(action.userId, action.user)
        }
      }
    } yield Either.right[ProgramError, Unit](unit)

  }

}
