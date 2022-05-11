package actor.user

import actor.person.domain.PersonCreated
import actor.user
import actor.user.domain._
import com.akkaserverless.scalasdk.eventsourcedentity.EventSourcedEntity
import com.akkaserverless.scalasdk.eventsourcedentity.EventSourcedEntityContext
import com.google.protobuf.empty.Empty
import actor.messaging.domain.SendMessageDtoM
// This class was initially generated based on the .proto definition by Akka Serverless tooling.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

class User(context: EventSourcedEntityContext) extends AbstractUser {
  override def emptyState: UserState = UserState()

  override def receiveMessage(
      currentState: UserState,
      sendMessageDto: SendMessageDto
  ): EventSourcedEntity.Effect[Empty] = {
    println(s"[DEBUG] User.receiveMessage ${sendMessageDto.userId} received ${sendMessageDto.message}")
    effects
      .emitEvent(
        ReceiveMessageDto(
          userId = sendMessageDto.userId,
          message = sendMessageDto.message
        )
      )
      .thenReply(_ => Empty())
  }

  override def receiveMessageDto(currentState: UserState, receiveMessageDto: ReceiveMessageDto): UserState = {
    println(s"[DEBUG] User.receiveMessageDto ${receiveMessageDto.userId} received ${receiveMessageDto.message}")
    currentState.copy(
      messages = currentState.messages appended receiveMessageDto.message
    )
  }

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
        name = currentState.name,
        persons = currentState.persons,
        messages = currentState.messages
      )
    )

  override def userCreated(currentState: UserState, userCreated: UserCreated): UserState =
    currentState.copy(
      name = userCreated.name
    )

  override def addedPerson(currentState: UserState, addedPerson: AddedPerson): UserState = {
    println("User.addedPerson -- before", "   ", addedPerson.userId, currentState.name, currentState.persons)

    val a = UserState(
      name = currentState.name,
      persons = Seq(addedPerson.personId)
    )
    println("User.addedPerson -- after", "   ", addedPerson.userId, a.name, a.persons)

    a
  }

  override def addPerson(currentState: UserState, personCreated: PersonCreated): EventSourcedEntity.Effect[Empty] = {
    effects
      .emitEvent(
        AddedPerson(
          personCreated.userId,
          personCreated.personId
        )
      )
      .thenReply(_ => Empty())
  }
}
