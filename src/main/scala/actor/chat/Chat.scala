package actor.chat

import actor.chat.domain.{ ChatState, Message, MessageAdded }
import com.akkaserverless.scalasdk.eventsourcedentity.EventSourcedEntity
import com.akkaserverless.scalasdk.eventsourcedentity.EventSourcedEntityContext
import com.google.protobuf.empty.Empty

// This class was initially generated based on the .proto definition by Akka Serverless tooling.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

class Chat(context: EventSourcedEntityContext) extends AbstractChat {
  override def emptyState: ChatState =
    ChatState(
      messages = Seq.empty[Message]
    )
  override def addMessage(
      currentState: ChatState,
      addMessageCommand: AddMessageCommand
  ): EventSourcedEntity.Effect[Empty] =
    effects
      .emitEvent(
        MessageAdded(
          chatId = addMessageCommand.chatId,
          message = Option(Message(addMessageCommand.message))
        )
      )
      .thenReply(state => Empty())

  override def getChat(currentState: ChatState, getChatCommand: GetChatCommand): EventSourcedEntity.Effect[ChatView] =
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
}
