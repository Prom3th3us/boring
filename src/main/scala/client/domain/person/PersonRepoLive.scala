package client.domain.person

import arch.common.Program.{ Context, MError }
import arch.infra.repo.RepoF
import client.domain.person.model.Person
import client.domain.person.model.Person.PersonId
import client.infrastructure.ProgramLive._

object PersonRepoLive {

  abstract class PersonRepoF[F[_]: MError] extends RepoF[F](Context("person-repo")) {
    override type Key   = PersonId
    override type Value = Person
  }
  object PersonRepoF {
    def apply[F[_]](implicit repo: PersonRepoF[F]): PersonRepoF[F] = repo
  }

  object PersonRepoApp  extends PersonRepoF[App]
  object PersonRepoTest extends PersonRepoF[Test]
}
