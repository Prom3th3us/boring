package client.domain.chat.service

import arch.common.Program.{ Context, MError, ProgramError }
import arch.infra.repo.Repo.*
import arch.infra.router.Action
import cats.data.OptionT
import cats.implicits.*
import cats.instances.*
import client.domain.chat.ChatRepoLive.ChatRepoF
import client.domain.chat.model.Chat
import client.domain.chat.model.Chat.*
import client.domain.user.UserRepoLive.UserRepoF
import client.domain.user.model.User
import client.domain.user.model.User.*

case class Find(id: ChatId) extends Action[Chat]

object Find {

  implicit def handler[F[_]: MError: ChatRepoF]: Action.Handler[F, Find] =
    action =>
      for {
        result <- find(action.id)
        user   <- MError[F].fromOption(result, ProgramError.simple("chat not found"))
      } yield user

}
