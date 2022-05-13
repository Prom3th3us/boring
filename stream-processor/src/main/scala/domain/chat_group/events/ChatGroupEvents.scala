package domain.chat_group.events

import domain.chat_group.entities.entities.UserId

sealed trait ChatGroupEvents
object ChatGroupEvents {
  case class AddedUserToChat(userId: UserId)     extends ChatGroupEvents
  case class DeletedUserFromChat(userId: UserId) extends ChatGroupEvents
  // case class SentMessageToChat(chatGroupId: ChatGroupId, message: Message) extends ChatGroupEvents
  // case class SentMessageToUser(userId: UserId, message: Message)           extends ChatGroupEvents

}
