package actor.user

import actor.person.domain.PersonCreated
import actor.user
import actor.user.domain.UserCreated
import actor.user.domain.UserState
import com.akkaserverless.scalasdk.eventsourcedentity.EventSourcedEntity
import com.akkaserverless.scalasdk.eventsourcedentity.EventSourcedEntityContext
import com.google.protobuf.empty.Empty

// This class was initially generated based on the .proto definition by Akka Serverless tooling.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

class User(context: EventSourcedEntityContext) extends AbstractUser {
  override def emptyState: UserState = UserState()

  override def createUser(
      currentState: UserState,
      createUserCommand: CreateUserCommand
  ): EventSourcedEntity.Effect[Empty] =
    effects
      .emitEvent(
        UserCreated(
          userId = createUserCommand.userId,
          name = createUserCommand.name
        )
      )
      .thenReply(state => Empty())

  override def getUser(currentState: UserState, getUserCommand: GetUserCommand): EventSourcedEntity.Effect[UserView] =
    effects.reply(
      UserView(
        name = currentState.name
      )
    )

  override def userCreated(currentState: UserState, userCreated: UserCreated): UserState =
    currentState.copy(
      name = userCreated.name
    )

  override def addPerson(currentState: UserState, personCreated: PersonCreated): EventSourcedEntity.Effect[Empty] = {
    println("OVER HERE at addPerson from User!")
    effects.reply(Empty())
  }
}
