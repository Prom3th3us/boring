package client.domain.user.service.find

import arch.infra.router.Action
import client.domain.user.UserRepoLive.UserRepoF
import client.domain.user.model.User
import client.domain.user.model.User._
import arch.infra.repo.Repo._
case class FindUserAction(id: String) extends Action[Option[User]]

object FindUserAction {
  implicit def handler[F[_]: UserRepoF]: FindUserAction => F[Option[User]] =
    action => find(UserId(action.id))
}
