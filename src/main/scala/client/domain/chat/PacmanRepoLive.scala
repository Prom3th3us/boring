package client.domain.chat

import arch.common.Program.{ Context, MError }
import arch.infra.repo.RepoF
import client.domain.chat.model.Chat
import client.domain.chat.model.Chat._
import client.domain.chat.model.Chat.Message.MessageId
import client.domain.person.model.Person
import client.domain.person.model.Person.PersonId
import client.infrastructure.ProgramLive.*
import collection.mutable

object PacmanRepoLive {

  abstract class PacmanRepoF[F[_]: MError] extends RepoF[F](Context("chat-repo")) {
    override type Key   = PersonInChatRoom
    override type Value = Message

    type MyPacman = PacmanF[F, String, Message, PersonId]

    private val pacmans = mutable.Map.empty[ChatId, MyPacman]
    private def getMeAPacman(chatId: ChatId): MyPacman =
      pacmans.get(chatId) match {
        case Some(pacman) => pacman
        case None =>
          val newPacman = new PacmanF[F, String, Message, PersonId](
            messagesPerUser = mutable.HashMap.empty[PersonId, mutable.Queue[Message]]
          )
          pacmans
            .addOne(chatId -> newPacman)
          newPacman
      }

    override def set(id: PersonInChatRoom, message: Message): F[Unit] =
      getMeAPacman(
        id.chatId
      ).add(message, id.chat.get.persons)
    override def get(id: PersonInChatRoom): F[Option[Message]] =
      getMeAPacman(
        id.chatId
      ).readLastMessage(
        id.personId,
        1
      )

  }

  object PacmanRepoF {
    def apply[F[_]](implicit repo: PacmanRepoF[F]): PacmanRepoF[F] = repo
  }

  object PacmanRepoApp extends PacmanRepoF[App]

  object PacmanRepoTest extends PacmanRepoF[Test]
}
