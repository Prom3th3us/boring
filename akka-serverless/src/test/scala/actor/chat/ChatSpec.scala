package actor.chat

import actor.chat
import actor.chat.domain.{ ChatState, MessageAdded }
import com.akkaserverless.scalasdk.eventsourcedentity.EventSourcedEntity
import com.akkaserverless.scalasdk.testkit.EventSourcedResult
import com.google.protobuf.empty.Empty
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

// This class was initially generated based on the .proto definition by Akka Serverless tooling.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

class ChatSpec extends AnyWordSpec with Matchers {
  "The Chat" should {

    "correctly process commands of type AddMessage" in {
      val testKit                         = ChatTestKit(new Chat(_))
      val chatId                          = "chat A"
      val message                         = "hello"
      val done: EventSourcedResult[Empty] = testKit.addMessage(AddMessageCommand(chatId, message))
      done.reply shouldBe Empty()
      done.events shouldBe Seq(MessageAdded(chatId, Some(domain.Message(message))))
    }

    "correctly process commands of type GetChat" in {
      val testKit = ChatTestKit(new Chat(_))
      val chatId  = "chat A"
      val message = "hello"
      val result: EventSourcedResult[ChatView] = {
        testKit.addMessage(AddMessageCommand(chatId, message))
        testKit.getChat(GetChatCommand(chatId))
      }

      result.reply shouldBe ChatView(Seq(MessageView(message)))
    }
  }
}
