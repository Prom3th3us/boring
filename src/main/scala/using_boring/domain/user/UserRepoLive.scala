package using_boring.domain.user

import arch.common.Program.{Context, MError}
import using_boring.common.repo.RepoF
import using_boring.domain.user.model.User
import using_boring.infrastructure.ProgramLive._

object UserRepoLive {

  abstract class UserRepoF[F[_]: MError]
      extends RepoF[F, String, User](Context("user-repo"))
  object UserRepoF {
    def apply[F[_]](implicit repo: UserRepoF[F]): UserRepoF[F] = repo
  }

  object UserRepoApp extends UserRepoF[App]
  object UserRepoTest extends UserRepoF[Test]
}
