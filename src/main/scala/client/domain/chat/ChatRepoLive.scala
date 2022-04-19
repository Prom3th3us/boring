package client.domain.chat

import arch.common.Program.{ Context, MError }
import arch.infra.repo.RepoF
import client.domain.chat.model.Chat
import client.domain.chat.model.Chat.*
import client.domain.chat.model.Chat.Message.MessageId
import client.domain.person.model.Person
import client.domain.person.model.Person.PersonId
import client.infrastructure.ProgramLive.*

import scala.collection.mutable

object ChatRepoLive {

  abstract class ChatRepoF[F[_]: MError] extends RepoF[F](Context("chat-repo")) {
    override type Key   = ChatId
    override type Value = Chat
  }

  object ChatRepoF {
    def apply[F[_]](implicit repo: ChatRepoF[F]): ChatRepoF[F] = repo
  }

  object ChatRepoApp extends ChatRepoF[App]

  object ChatRepoTest extends ChatRepoF[Test]
}
