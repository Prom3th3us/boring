package actor.person

import actor.person
import actor.person.domain.PersonState
import com.akkaserverless.scalasdk.valueentity.ValueEntity
import com.akkaserverless.scalasdk.valueentity.ValueEntityContext
import com.google.protobuf.empty.Empty

// This class was initially generated based on the .proto definition by Akka Serverless tooling.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

class Person(context: ValueEntityContext) extends AbstractPerson {
  override def emptyState: PersonState =
    throw new UnsupportedOperationException("Not implemented yet, replace with your empty entity state")

  override def register(currentState: PersonState, registerPersonValue: RegisterPersonValue): ValueEntity.Effect[Empty] =
    effects.error("The command handler for `register` is not implemented, yet")

  override def find(currentState: PersonState, findPersonValue: FindPersonValue): ValueEntity.Effect[PersonGetResponse] =
    effects.error("The command handler for `find` is not implemented, yet")

}

