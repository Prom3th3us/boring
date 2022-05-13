package domain.chat_group.state

import domain.State
import domain.chat_group.entities.entities.UserId
import domain.chat_group.events.ChatGroupEvents

case class ChatGroupState(
    users: Set[UserId]
) extends State[ChatGroupEvents, ChatGroupState] {
  override def +(event: ChatGroupEvents): ChatGroupState = {
    event match {
      case ChatGroupEvents.AddedUserToChat(userId) =>
        copy(users = users + userId)

      case ChatGroupEvents.DeletedUserFromChat(userId) =>
        copy(users = users - userId)

      // case ChatGroupEvents.SentMessageToChat(chatGroupId, message) => ???
      // case ChatGroupEvents.SentMessageToUser(userId, message)      => ???
    }
  }
}
