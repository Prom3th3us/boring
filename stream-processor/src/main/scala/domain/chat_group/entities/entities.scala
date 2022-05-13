package domain.chat_group.entities

object entities {
  sealed trait Entity
  case class UserId(id: String)      extends Entity
  case class ChatGroupId(id: String) extends Entity
  case class Message(text: String)   extends Entity

}
