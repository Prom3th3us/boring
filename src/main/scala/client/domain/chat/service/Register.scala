package client.domain.chat.service

import arch.common.Program.MError.*
import arch.common.Program.{ Context, MError, ProgramError }
import arch.infra.repo.Repo.*
import arch.infra.router.Router.*
import arch.infra.router.{ Action, RouterF }
import cats.implicits.*
import client.domain.chat.ChatRepoLive.ChatRepoF
import client.domain.chat.model.*
import client.domain.chat.model.Chat.*
case class Register(chatId: ChatId, chat: Chat) extends Action[Either[ProgramError, Unit]]

object Register {
  private val ctx = Context("register-chat-action")

  implicit def handler[F[_]: MError: ChatRepoF: RouterF]: Action.Handler[F, Register] =
    action =>
      for {
        maybeChat: Option[Chat] <- find(action.chatId)
        unit: Unit <- {
          maybeChat match {
            case Some(chat) =>
              MError[F].raiseError(
                ProgramError(
                  error = new Exception("chat already exists"),
                  ctx = ctx,
                  msg = s"chat already exists: $chat"
                )
              )
            case None => set(action.chatId, action.chat)
          }
        }
      } yield Either.right[ProgramError, Unit](unit)

}
