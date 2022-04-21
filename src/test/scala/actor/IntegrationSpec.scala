package actor

import actor.chat.ChatService
import actor.user.{ CreateUserCommand, GetUserCommand, UserService }
import actor.person.{ CreatePersonCommand, GetPersonCommand, PersonService, PersonView }
import akka.actor.ActorSystem
import com.akkaserverless.scalasdk.testkit.{ AkkaServerlessTestKit, EventSourcedResult }
import com.google.protobuf.empty.Empty
import org.scalatest.BeforeAndAfterAll
import org.scalatest.concurrent.{ Eventually, ScalaFutures }
import org.scalatest.matchers.should.Matchers
import org.scalatest.time.{ Millis, Seconds, Span }
import org.scalatest.wordspec.AnyWordSpec

import scala.concurrent.Await
import scala.language.postfixOps

// This class was initially generated based on the .proto definition by Akka Serverless tooling.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

class IntegrationSpec extends AnyWordSpec with Matchers with BeforeAndAfterAll with ScalaFutures with Eventually {

  implicit private val patience: PatienceConfig =
    PatienceConfig(Span(150, Seconds), Span(15000, Millis))

  private val testKit = AkkaServerlessTestKit(Main.createAkkaServerless()).start()

  private val chat: ChatService     = testKit.getGrpcClient[ChatService](classOf[ChatService])
  private val user: UserService     = testKit.getGrpcClient[UserService](classOf[UserService])
  private val person: PersonService = testKit.getGrpcClient[PersonService](classOf[PersonService])

  import testKit.executionContext

  "ChatService" must {

    "have example test that can be removed" in {

      // use the gRPC client to send requests to the
      // proxy and verify the results

      val userId   = "user A"
      val personId = "person A"
      val name     = "person name"

      user.createUser(CreateUserCommand(userId, "user name")).futureValue

      val e = for {
        _ <- person.createPerson(CreatePersonCommand(userId, personId, name))
        e <- person.getPerson(GetPersonCommand(userId, personId))
      } yield e

      e.futureValue.name shouldBe name

      eventually {
        val resultingUser = user.getUser(GetUserCommand(userId)).futureValue
        println("user.getUser: ", resultingUser.name, resultingUser.persons, resultingUser.persons == Seq(personId))
        resultingUser.persons shouldBe Seq(personId)
      }
    }

  }

  override def afterAll(): Unit = {
    testKit.stop()
    super.afterAll()
  }
}
