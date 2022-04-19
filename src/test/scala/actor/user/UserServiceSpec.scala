package actor.user

import actor.user
import actor.user.domain.UserState
import com.akkaserverless.scalasdk.testkit.ValueEntityResult
import com.akkaserverless.scalasdk.valueentity.ValueEntity
import com.google.protobuf.empty.Empty
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class UserServiceSpec
    extends AnyWordSpec
    with Matchers {

  "UserService" must {

    "have example test that can be removed" in {
      val testKit = UserServiceTestKit(new UserService(_))
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
      val testKit = UserServiceTestKit(new UserService(_))
      // val result = testKit.register(RegisterUserValue(...))
    }

    "handle command find" in {
      val testKit = UserServiceTestKit(new UserService(_))
      // val result = testKit.find(FindUserValue(...))
    }

    "handle command addPerson" in {
      val testKit = UserServiceTestKit(new UserService(_))
      // val result = testKit.addPerson(AddPersonValue(...))
    }

  }
}