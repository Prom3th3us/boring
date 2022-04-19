package actor.user

import actor.user
import actor.user.domain.UserState
import com.akkaserverless.scalasdk.valueentity.ValueEntity
import com.akkaserverless.scalasdk.valueentity.ValueEntityContext
import com.google.protobuf.empty.Empty

// This class was initially generated based on the .proto definition by Akka Serverless tooling.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

class User(context: ValueEntityContext) extends AbstractUser {
  override def emptyState: UserState =
    UserState(1)

  override def register(currentState: UserState, registerUserValue: RegisterUserValue): ValueEntity.Effect[Empty] =
    sdk.user.register(registerUserValue.id, registerUserValue.name) match {
      case Left(value)  => effects.error(s"error: ${error}")
      case Right(value) => effects.reply(value)
    }

  override def find(currentState: UserState, findUserValue: FindUserValue): ValueEntity.Effect[UserGetResponse] =
    sdk.user.find(findUserValue.id) match {
      case Left(value)  => effects.error(s"error: ${error}")
      case Right(value) => effects.reply(value)
    }
  override def addPerson(currentState: UserState, addPersonValue: AddPersonValue): ValueEntity.Effect[Empty] =
    sdk.user.add(id = addPersonValue.id, name = addPersonValue.personId) match {
      case Left(error)  => effects.error(s"error: ${error}")
      case Right(value) => effects.reply(value)
    }
}
