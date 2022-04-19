package actor.person

import actor.person
import actor.person.domain.PersonState
import com.akkaserverless.scalasdk.testkit.ValueEntityResult
import com.akkaserverless.scalasdk.valueentity.ValueEntity
import com.google.protobuf.empty.Empty
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class PersonSpec
    extends AnyWordSpec
    with Matchers {

  "Person" must {

    "have example test that can be removed" in {
      val testKit = PersonTestKit(new Person(_))
      // use the testkit to execute a command
      // and verify final updated state:
      // val result = testKit.someOperation(SomeRequest)
      // verify the response
      // val actualResponse = result.getReply()
      // actualResponse shouldBe expectedResponse
      // verify the final state after the command
      // testKit.currentState() shouldBe expectedState
    }

    "handle command register" in {
      val testKit = PersonTestKit(new Person(_))
      // val result = testKit.register(RegisterPersonValue(...))
    }

    "handle command find" in {
      val testKit = PersonTestKit(new Person(_))
      // val result = testKit.find(FindPersonValue(...))
    }

  }
}
