package actor.messaging

import com.akkaserverless.scalasdk.action.Action
import com.akkaserverless.scalasdk.action.ActionCreationContext

// This class was initially generated based on the .proto definition by Akka Serverless tooling.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

class MessagingServiceAction(creationContext: ActionCreationContext) extends AbstractMessagingServiceAction {

  override def sendMessage(
      sendMessageCommand: actor.messaging.SendMessageDtoM
  ): Action.Effect[actor.messaging.SendMessageDtoM] = {
    println(
      s"[DEBUG] MessagingServiceAction.sendMessage ${sendMessageCommand.userId} sent ${sendMessageCommand.message}"
    )
    effects.reply(
      sendMessageCommand
    )
  }
}
