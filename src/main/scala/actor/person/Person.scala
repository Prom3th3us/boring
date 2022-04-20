package actor.person

import actor.person
import actor.person.domain.PersonCreated
import actor.person.domain.PersonState
import com.akkaserverless.scalasdk.eventsourcedentity.EventSourcedEntity
import com.akkaserverless.scalasdk.eventsourcedentity.EventSourcedEntityContext
import com.google.protobuf.empty.Empty

// This class was initially generated based on the .proto definition by Akka Serverless tooling.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

class Person(context: EventSourcedEntityContext) extends AbstractPerson {
  override def emptyState: PersonState =
    PersonState()
  override def createPerson(
      currentState: PersonState,
      createPersonCommand: CreatePersonCommand
  ): EventSourcedEntity.Effect[Empty] =
    effects
      .emitEvent(
        PersonCreated(
          userId = createPersonCommand.userId,
          personId = createPersonCommand.personId,
          name = createPersonCommand.name
        )
      )
      .thenReply(state => Empty())

  override def getPerson(
      currentState: PersonState,
      getPersonCommand: GetPersonCommand
  ): EventSourcedEntity.Effect[PersonView] =
    effects.reply(PersonView(currentState.name))

  override def personCreated(currentState: PersonState, personCreated: PersonCreated): PersonState =
    currentState.copy(
      name = personCreated.name
    )
}
