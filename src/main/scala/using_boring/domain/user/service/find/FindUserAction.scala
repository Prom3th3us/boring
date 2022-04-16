package using_boring.domain.user.service.find

import arch.infra.router.Action
import using_boring.domain.user.UserRepoLive.UserRepoF
import using_boring.domain.user.model.User

case class FindUserAction(id: String) extends Action[Option[User]]

object FindUserAction {
  implicit def handler[F[_]: UserRepoF]: FindUserAction => F[Option[User]] = {
    (action: FindUserAction) =>
      UserRepoF[F].get(action.id)
  }
}
