package client.domain.user.service

import arch.common.Program.MError.*
import arch.common.Program.{ Context, MError, ProgramError }
import arch.infra.repo.Repo.*
import arch.infra.router.Router.*
import arch.infra.router.{ Action, RouterF }
import cats.implicits.*
import client.domain.user.UserRepoLive.UserRepoF
import client.domain.user.model.*
import client.domain.user.model.User.*
case class Register(userId: UserId, user: User) extends Action[Either[ProgramError, Unit]]

object Register {
  private val ctx = Context("register-user-action")

  implicit def handler[F[_]: MError: UserRepoF: RouterF]: Action.Handler[F, Register] =
    action =>
      for {
        maybeUser: Option[User] <- find(action.userId)
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
