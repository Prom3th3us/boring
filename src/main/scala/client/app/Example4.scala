package client.app

import arch.common.Program.MError
import arch.infra.repo.Repo
import arch.infra.router.{ Action, RouterF }
import client.domain.user.UserRepoLive.{ UserRepoF, UserRepoTest }
import client.domain.user.model.User
import client.domain.user.model.User._
import client.domain.user.service.find.FindUserAction
import client.infrastructure.ProgramLive.Test
import cats.effect

object Example4 extends App {

  import example._
  implicit lazy val userRepoLive: UserRepoF[Test] = UserRepoTest

  case class FindUserActionHandler[F[_]: UserRepoF: MError]() extends Action.Handler[F, FindUserAction] {
    override def handle(action: FindUserAction) = find(UserId(action.id))
  }

  print(find(UserId("user A")))

  object example:

    def find[F[_], r <: Repo[F]](using rr: r)(key: rr.Key): F[Option[rr.Value]] =
      rr.get(key)

    def set[F[_], r <: Repo[F]](using rr: r)(key: rr.Key, value: rr.Value): F[Unit] =
      rr.set(key, value)

}
