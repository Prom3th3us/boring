package actor.messaging

import com.akkaserverless.scalasdk.action.Action
import com.akkaserverless.scalasdk.testkit.ActionResult
import com.google.protobuf.empty.Empty
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

// This class was initially generated based on the .proto definition by Akka Serverless tooling.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

class MessagingServiceActionSpec
    extends AnyWordSpec
    with Matchers {

  "MessagingServiceAction" must {

    "have example test that can be removed" in {
      val testKit = MessagingServiceActionTestKit(new MessagingServiceAction(_))
      // use the testkit to execute a command
      // and verify final updated state:
      // val result = testKit.someOperation(SomeRequest)
      // verify the response
      // result.reply shouldBe expectedReply
    }

    "handle command SendMessage" in {
      val testKit = MessagingServiceActionTestKit(new MessagingServiceAction(_))
      // val result = testKit.sendMessage(SendMessageCommand(...))
    }

  }
}
