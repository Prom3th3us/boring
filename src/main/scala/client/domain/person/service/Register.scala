package client.domain.person.service

import arch.common.Program.MError.*
import arch.common.Program.{ Context, MError, ProgramError }
import arch.infra.repo.Repo.*
import arch.infra.router.Router.*
import arch.infra.router.{ Action, RouterF }
import cats.implicits.*
import client.domain.person.PersonRepoLive.PersonRepoF
import client.domain.person.model.*
import client.domain.person.model.Person.*
case class Register(personId: PersonId, person: Person) extends Action[Either[ProgramError, Unit]]

object Register {
  private val ctx = Context("register-person-action")

  implicit def handler[F[_]: MError: PersonRepoF: RouterF]: Action.Handler[F, Register] =
    action =>
      for {
        maybePerson: Option[Person] <- find(action.personId)
        unit: Unit <- {
          maybePerson match {
            case Some(person) =>
              MError[F].raiseError(
                ProgramError(
                  error = new Exception("person already exists"),
                  ctx = ctx,
                  msg = s"person already exists: $person"
                )
              )
            case None => set(action.personId, action.person)
          }
        }
      } yield Either.right[ProgramError, Unit](unit)

}
