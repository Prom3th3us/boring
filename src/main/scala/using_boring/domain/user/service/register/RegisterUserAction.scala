package using_boring.domain.user.service.register

import arch.common.Program.{Context, MError, ProgramError}
import arch.infra.router.{Action, DispatcherF, RouterF}
import cats.implicits.*
import using_boring.domain.user.UserRepoLive.UserRepoF
import using_boring.domain.user.model.User
import using_boring.domain.user.service.find.FindUserAction

case class RegisterUserAction(userId: String)
    extends Action[Either[ProgramError, Unit]]

object RegisterUserAction {
  private val ctx = Context("register-user-action")

  implicit def handler[
      F[_]: MError: UserRepoF: RouterF
  ]: RegisterUserAction => F[Either[ProgramError, Unit]] = {
    (action: RegisterUserAction) =>
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
            case None => UserRepoF[F].set(action.userId, User(action.userId))
          }
        }
      } yield Either.right[ProgramError, Unit](unit)
  }
}
