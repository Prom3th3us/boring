package client.domain.chat.service;

import arch.common.Program.{ Context, MError, ProgramError }
import arch.infra.repo.Repo.set
import arch.infra.router.Router.publish
import arch.infra.router.RouterF
import client.domain.person.model.Person.PersonId
import client.domain.person.PersonRepoLive.PersonRepoF
import client.domain.chat.ChatRepoLive.ChatRepoF
import client.domain.chat.model.Chat
import client.domain.person.model.Person
import client.domain.chat.model.Chat.ChatId
import client.domain.person.{ service => personService }
import client.domain.chat.{ service => chatService }
import arch.infra.router.Action
import cats.implicits._
import cats.instances._
import cats.data._

case class AddPerson(chatId: ChatId, personId: PersonId) extends Action[Unit]

object AddPerson:
  private val ctx = Context("chat-add-person")

  implicit def handler[F[_]: MError: ChatRepoF: RouterF]: Action.Handler[F, AddPerson] = { (action: AddPerson) =>
    for {
      person: Person <- publish(personService.Find(action.personId))
      chat: Chat     <- publish(chatService.Find(action.chatId))
      added: Unit <-
        set(
          action.chatId,
          chat.copy(
            persons = chat.persons + action.personId
          )
        )
    } yield added

  }
