package client.domain.chat.service

import arch.common.Program.{ MError, ProgramError }
import arch.infra.repo.Repo.set
import client.domain.chat.model.Chat
import client.domain.chat.model.Chat.*
import arch.infra.router.Action
import client.domain.chat.PacmanRepoLive.PacmanRepoF
import client.domain.chat.ChatRepoLive.ChatRepoF
import client.domain.person.model.Person.PersonId
import arch.infra.router.Router.*
import arch.infra.router.{ Action, RouterF }
import client.domain.chat.{ service => chatService }
import cats.implicits._

case class SendMessage(personId: PersonId, chatId: ChatId, message: Message) extends Action[Unit]

object SendMessage:
  implicit def handler[F[_]: MError: RouterF: PacmanRepoF]: Action.Handler[F, SendMessage] =
    action =>
      for {
        chatRoom: Chat <- RouterF[F].publish(chatService.Find(action.chatId))
        result <- PacmanRepoF[F].set(
          PersonInChatRoom(action.personId, action.chatId, Some(chatRoom)),
          action.message
        )
      } yield result
