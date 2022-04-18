package client.domain.user

import arch.common.Program.{ Context, MError }
import arch.infra.repo.RepoF
import client.domain.user.model.User
import client.domain.user.model.User.UserId
import client.infrastructure.ProgramLive._

object UserRepoLive {

  abstract class UserRepoF[F[_]: MError] extends RepoF[F](Context("user-repo")) {
    override type Key   = UserId
    override type Value = User
  }
  object UserRepoF {
    def apply[F[_]](implicit repo: UserRepoF[F]): UserRepoF[F] = repo
  }

  object UserRepoApp  extends UserRepoF[App]
  object UserRepoTest extends UserRepoF[Test]
}
