package client.domain.user.service.find

import arch.infra.router.Action
import client.domain.user.UserRepoLive.UserRepoF
import client.domain.user.model.User
import client.domain.user.model.User._
import arch.infra.repo.Repo._
case class FindUserAction(id: UserId) extends Action[Option[User]]

object FindUserAction {
  implicit def handler[F[_]: UserRepoF]: Action.Handler[F, FindUserAction] =
    action => find(action.id)
}
