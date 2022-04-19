package client.domain.person.service

import arch.common.Program.{ Context, MError, ProgramError }
import arch.infra.repo.Repo.*
import arch.infra.router.Action
import cats.data.OptionT
import cats.implicits.*
import cats.instances.*
import client.domain.person.PersonRepoLive.PersonRepoF
import client.domain.person.model.Person
import client.domain.person.model.Person.*
import client.domain.user.UserRepoLive.UserRepoF
import client.domain.user.model.User
import client.domain.user.model.User.*

case class Find(id: PersonId) extends Action[Person]

object Find {

  implicit def handler[F[_]: MError: PersonRepoF]: Action.Handler[F, Find] =
    action =>
      for {
        result <- find(action.id)
        user   <- MError[F].fromOption(result, ProgramError.simple("person not found"))
      } yield user

}
