package client.domain.chat.service

import arch.common.Program.{ MError, ProgramError }
import arch.infra.repo.Repo.{ find, set }
import client.domain.chat.model.Chat
import client.domain.chat.model.Chat.*
import client.domain.chat.{ service => chatService }
import arch.infra.router.Action
import client.domain.chat.PacmanRepoLive.PacmanRepoF
import client.domain.chat.ChatRepoLive.ChatRepoF
import cats.implicits._
import client.domain.person.model.Person.PersonId
import arch.infra.router.Router.*
import arch.infra.router.{ Action, RouterF }

case class ReadMessage(personId: PersonId, chatId: ChatId) extends Action[Message]

object ReadMessage:
  implicit def handler[F[_]: MError: PacmanRepoF: ChatRepoF: RouterF]: Action.Handler[F, ReadMessage] =
    action =>
      for {
        result  <- PacmanRepoF[F].get(PersonInChatRoom(action.personId, action.chatId, None))
        message <- MError[F].fromOption(result, ProgramError.simple("no more messages to read"))
      } yield message
