package actor.user

import actor.user._
import actor.user
import actor.user.domain.UserCreated
import com.akkaserverless.scalasdk.eventsourcedentity.EventSourcedEntity
import com.akkaserverless.scalasdk.testkit.EventSourcedResult
import com.google.protobuf.empty.Empty
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

// This class was initially generated based on the .proto definition by Akka Serverless tooling.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

class UserSpec extends AnyWordSpec with Matchers {
  "The User" should {

    "correctly process commands of type CreateUserCommand" in {
      val testKit                         = UserTestKit(new User(_))
      val userId                          = "user A"
      val name                            = "user name"
      val done: EventSourcedResult[Empty] = testKit.createUser(CreateUserCommand(userId, name))
      done.reply shouldBe Empty()
      done.events shouldBe Seq(UserCreated(userId, name))
    }

    "correctly process commands of type GetUser" in {
      val testKit = UserTestKit(new User(_))
      val userId  = "user A"
      val name    = "user name"
      val result: EventSourcedResult[UserView] = {
        testKit.createUser(CreateUserCommand(userId, name))
        testKit.getUser(GetUserCommand(userId))
      }

      result.reply shouldBe UserView(name)
    }
  }
}
