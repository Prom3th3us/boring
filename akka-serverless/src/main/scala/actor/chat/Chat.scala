package actor.chat

import actor.chat.domain.{
  AdminAdded,
  AdminRemoved,
  ChatState,
  CreatedChat,
  Message,
  MessageAdded,
  UserAdded,
  UserRemoved
}
import com.akkaserverless.scalasdk.eventsourcedentity.EventSourcedEntity
import com.akkaserverless.scalasdk.eventsourcedentity.EventSourcedEntityContext
import com.google.protobuf.empty.Empty

// This class was initially generated based on the .proto definition by Akka Serverless tooling.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

class Chat(context: EventSourcedEntityContext) extends AbstractChat {
  override def emptyState: ChatState =
    ChatState()

  override def createChat(currentState: ChatState, cmd: CreateChat): EventSourcedEntity.Effect[Empty] = {
    if (currentState != emptyState)
      effects.error("Chat already created")
    else
      effects
        .emitEvent(
          CreatedChat(
            cmd.chatId,
            cmd.admin
          )
        )
        .thenReply(_ => Empty())
  }

  override def createdChat(currentState: ChatState, evt: CreatedChat): ChatState =
    ChatState(
      admins = Seq(evt.admin)
    )

  override def addMessage(
      currentState: ChatState,
      addMessageCommand: AddMessage
  ): EventSourcedEntity.Effect[Empty] =
    effects
      .emitEvent(
        MessageAdded(
          chatId = addMessageCommand.chatId,
          message = Option(Message(addMessageCommand.message))
        )
      )
      .thenReply(_ => Empty())

  override def getChat(currentState: ChatState, getChatCommand: GetChat): EventSourcedEntity.Effect[ChatView] =
    effects.reply(
      ChatView(
        items = currentState.messages.map(_.text).map(t => MessageView(t))
      )
    )

  override def messageAdded(currentState: ChatState, messageAdded: MessageAdded): ChatState =
    messageAdded.message match {
      case None => currentState
      case Some(message) =>
        currentState.copy(
          messages = currentState.messages :+ Message(message.text)
        )
    }

  override def addUser(currentState: ChatState, cmd: AddUser): EventSourcedEntity.Effect[Empty] = {
    if ((currentState.admins.toSet union cmd.signedBy.toSet).isEmpty)
      effects.error("This operation needs an admin signature")
    else
      effects
        .emitEvent(
          UserAdded(
            cmd.chatId,
            cmd.userId,
            cmd.signedBy
          )
        )
        .thenReply(_ => Empty())
  }

  override def deleteUser(currentState: ChatState, cmd: RemoveUser): EventSourcedEntity.Effect[Empty] =
    if ((currentState.admins.toSet union cmd.signedBy.toSet).isEmpty)
      effects.error("This operation needs an admin signature")
    else
      effects
        .emitEvent(
          UserRemoved(
            cmd.chatId,
            cmd.userId,
            cmd.signedBy
          )
        )
        .thenReply(_ => Empty())

  override def addAdmin(currentState: ChatState, cmd: AddAdmin): EventSourcedEntity.Effect[Empty] =
    if ((currentState.admins.toSet union cmd.signedBy.toSet).isEmpty)
      effects.error("This operation needs an admin signature")
    else
      effects
        .emitEvent(
          AdminAdded(
            cmd.chatId,
            cmd.userId,
            cmd.signedBy
          )
        )
        .thenReply(_ => Empty())

  override def deleteAdmin(
      currentState: ChatState,
      cmd: RemoveAdmin
  ): EventSourcedEntity.Effect[Empty] =
    if ((currentState.admins.toSet union cmd.signedBy.toSet).isEmpty)
      effects.error("This operation needs an admin signature")
    else
      effects
        .emitEvent(
          AdminRemoved(
            cmd.chatId,
            cmd.userId,
            cmd.signedBy
          )
        )
        .thenReply(_ => Empty())

  override def adminAdded(currentState: ChatState, evt: AdminAdded): ChatState =
    currentState.copy(
      admins = currentState.admins :+ evt.userId
    )

  override def adminRemoved(currentState: ChatState, evt: AdminRemoved): ChatState =
    currentState.copy(
      admins = currentState.admins.filterNot(_ == evt.userId)
    )

  override def userAdded(currentState: ChatState, evt: UserAdded): ChatState =
    currentState.copy(
      users = currentState.users :+ evt.userId
    )

  override def userRemoved(currentState: ChatState, evt: UserRemoved): ChatState =
    currentState.copy(
      admins = currentState.admins.filterNot(_ == evt.userId)
    )

}
