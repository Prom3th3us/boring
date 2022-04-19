package client.domain.user.service

import arch.common.Program.{ Context, MError, ProgramError }
import arch.infra.repo.Repo.set
import arch.infra.router.Router.publish
import arch.infra.router.RouterF
import client.domain.person.model.Person.PersonId
import client.domain.user.UserRepoLive.UserRepoF
import client.domain.user.model.User
import client.domain.person.model.Person
import client.domain.user.model.User.UserId
import client.domain.user.{ service => userService }
import client.domain.person.{ service => personService }
import arch.infra.router.Action
import cats.implicits._
import cats.instances._
import cats.data._

case class AddPerson(userId: UserId, personId: PersonId) extends Action[Unit]

object AddPerson:
  private val ctx = Context("add-person-action")

  implicit def handler[F[_]: MError: UserRepoF: RouterF]: Action.Handler[F, AddPerson] = { (action: AddPerson) =>
    for {
      user: User     <- publish(userService.Find(action.userId))
      person: Person <- publish(personService.Find(action.personId))
      added: Unit <-
        set(
          action.userId,
          user.copy(
            persons = user.persons + action.personId
          )
        )
    } yield added

  }
