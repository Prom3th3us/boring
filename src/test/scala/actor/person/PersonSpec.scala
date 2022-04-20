package actor.person

import actor.person._
import actor.person
import actor.person.domain.PersonCreated
import com.akkaserverless.scalasdk.eventsourcedentity.EventSourcedEntity
import com.akkaserverless.scalasdk.testkit.EventSourcedResult
import com.google.protobuf.empty.Empty
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

// This class was initially generated based on the .proto definition by Akka Serverless tooling.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

class PersonSpec extends AnyWordSpec with Matchers {
  "The Person" should {

    "correctly process commands of type CreatePersonCommand" in {
      val testKit                         = PersonTestKit(new Person(_))
      val userId                          = "user A"
      val personId                        = "person A"
      val name                            = "person name"
      val done: EventSourcedResult[Empty] = testKit.createPerson(CreatePersonCommand(userId, personId, name))
      done.reply shouldBe Empty()
      done.events shouldBe Seq(PersonCreated(userId, personId, name))
    }

    "correctly process commands of type GetPerson" in {
      val testKit  = PersonTestKit(new Person(_))
      val userId   = "user A"
      val personId = "person A"
      val name     = "person name"
      val result: EventSourcedResult[PersonView] = {
        testKit.createPerson(CreatePersonCommand(userId, personId, name))
        testKit.getPerson(GetPersonCommand(userId, personId))
      }

      result.reply shouldBe PersonView(name)
    }
  }
}
