package domain.chat_group.commands

import domain.chat_group.entities.entities._

sealed trait ChatGroupCommands
object ChatGroupCommands {
  case class AddUser(userId: UserId)            extends ChatGroupCommands
  case class DeleteUserFromChat(userId: UserId) extends ChatGroupCommands
  // case class SendMessageToChat(chatGroupId: ChatGroupId, message: Message) extends ChatGroupCommands
  // case class SendMessageToUser(userId: UserId, message: Message)           extends ChatGroupCommands
}
