package client.domain.chat.model

import arch.common.HasId
import client.domain.person.model.Person.PersonId
import client.domain.chat.model.Chat.Message.MessageId

case class Chat(
    // TODO admins: Set[PersonId],
    persons: Set[PersonId]
)

object Chat {

  case class PersonInChatRoom(personId: PersonId, chatId: ChatId, chat: Option[Chat])

  case class ChatId(id: String) extends HasId[String]

  case class Message(
      id: String,
      from: String,
      // TODO timestamp: String,
      text: String
      // TODO deletable: Boolean
  ) extends HasId[String]
  object Message:
    case class MessageId(id: String) extends HasId[String]
}
