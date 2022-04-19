package com.example.domain

import client.domain.user.service.Find
import com.akkaserverless.scalasdk.valueentity.ValueEntity
import com.akkaserverless.scalasdk.valueentity.ValueEntityContext
import com.example
import com.example.{ GetUserValue, RegisterUserValue }
import com.google.protobuf.empty.Empty

import scala.util.{ Failure, Success }

// This class was initially generated based on the .proto definition by Akka Serverless tooling.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

class Counter(context: ValueEntityContext) extends AbstractCounter {

  import client.app.ProgramBuilder.ProgramBuilder
  import arch.common.Program.ProgramError
  import cats.effect.unsafe.IORuntime
  import com.typesafe.config.{ Config, ConfigFactory }
  import client.domain.user.model.User
  import client.domain.user.model.User._
  import client.domain.person.model.Person
  import client.domain.person.model.Person._
  import client.domain.chat.model.Chat
  import client.domain.chat.model.Chat._
  import client.domain.user.{ service => userService }
  import client.domain.person.{ service => personService }
  import client.domain.chat.{ service => chatService }
  import client.infrastructure.ProgramLive
  import arch.common.Program.MError
  import client.domain.user.service

  println(s"RUNNING ENV=test")
  // DEFINITIONS
  type Env[A] = ProgramLive.Test[A]
  // EXECUTION
  import client.app.Runner.config
  val router = implicitly[ProgramBuilder[Env]].buildApp(config)

  import scala.language.implicitConversions
  implicit def toUserId(id: String): UserId     = UserId(s"user-$id")
  implicit def toPersonId(id: String): PersonId = PersonId(s"person-$id")
  implicit def toChatId(id: String): ChatId     = ChatId(s"person-$id")

  override def emptyState: CounterState =
    throw new UnsupportedOperationException("Not implemented yet, replace with your empty entity state")

  override def registerUser(
      currentState: CounterState,
      registerUserValue: RegisterUserValue
  ): ValueEntity.Effect[Empty] = {

    def sdk(id: String, name: String) = {
      scala.util.Try {
        for {
          _ <- router.publish[Either[ProgramError, Unit], userService.Register](
            userService.Register(id, User(name, Set.empty[PersonId]))
          )
        } yield ()
      } match {
        case Failure(exception) =>
          println(s"""
              |
              |
              |Failure: 
              |  because: ${exception.getMessage}
              |""".stripMargin)
          throw exception
        case Success(value) =>
          value
      }
    }
    sdk(
      id = registerUserValue.id,
      name = registerUserValue.name
    )

    effects.reply(Empty())
  }

  override def getUser(currentState: CounterState, getUserValue: GetUserValue): ValueEntity.Effect[example.User] = {
    def sdk(id: String) = {
      for {
        found <- router.publish[User, userService.Find](
          userService.Find(id)
        )
      } yield found
    }

    sdk(
      id = getUserValue.id
    ) match {
      case Left(programError) =>
        effects.error(s"User not found -- ${programError}")
      case Right(output) =>
        effects.reply(
          example.User(
            id = getUserValue.id,
            name = output.name
          )
        )
    }

  }
  override def decrease(currentState: CounterState, decreaseValue: example.DecreaseValue): ValueEntity.Effect[Empty] =
    effects.error("The command handler for `Decrease` is not implemented, yet")

  override def reset(currentState: CounterState, resetValue: example.ResetValue): ValueEntity.Effect[Empty] =
    effects.error("The command handler for `Reset` is not implemented, yet")

  override def getCurrentCounter(
      currentState: CounterState,
      getCounter: example.GetCounter
  ): ValueEntity.Effect[example.CurrentCounter] =
    effects.error("The command handler for `GetCurrentCounter` is not implemented, yet")

}

/*

curl -XPOST -H "Content-Type: application/json" 0.0.0.0:9000 -d ''

 */
