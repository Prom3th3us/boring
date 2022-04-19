package client.app

import arch.common.Program.ProgramError
import cats.effect.unsafe.IORuntime
import com.typesafe.config.{ Config, ConfigFactory }
import client.domain.user.model.User
import client.domain.user.model.User.*
import client.domain.person.model.Person
import client.domain.person.model.Person.*
import client.domain.chat.model.Chat
import client.domain.chat.model.Chat.*
import client.domain.user.{ service => userService }
import client.domain.person.{ service => personService }
import client.domain.chat.{ service => chatService }
import client.infrastructure.ProgramLive
import arch.common.Program.MError
import client.domain.user.service

object Runner {
  import ProgramBuilder._

  import scala.jdk.CollectionConverters._

  val prod = false
  val config: Config = ConfigFactory.parseMap(
    Map(
      "user.flag"  -> true,
      "user.value" -> 1
    ).asJava
  )
  implicit val ioRuntime: IORuntime = cats.effect.unsafe.implicits.global

  def main(args: Array[String]): Unit = {
    val result = if (prod) {
      println(s"RUNNING ENV=prod")
      // DEFINITIONS
      type Env[A] = ProgramLive.App[A]
      // EXECUTION
      val router = implicitly[ProgramBuilder[Env]].buildApp(config)
      val actionResult = router.publish(
        userService.Find(UserId("Prom3th3us"))
      )
      // OUTPUT
      actionResult.value.unsafeRunSync()
    } else {
      println(s"RUNNING ENV=test")
      // DEFINITIONS
      type Env[A] = ProgramLive.Test[A]
      // EXECUTION
      val router = implicitly[ProgramBuilder[Env]].buildApp(config)

      import scala.language.implicitConversions
      implicit def toUserId(id: String): UserId     = UserId(s"user-$id")
      implicit def toPersonId(id: String): PersonId = PersonId(s"person-$id")
      implicit def toChatId(id: String): ChatId     = ChatId(s"person-$id")

      def registerPerson(id: String) = {
        for {
          _ <- router.publish(
            userService.Register(id, User(id, Set.empty[PersonId]))
          )
          _ <- router.publish(
            personService.Register(id, Person(id))
          )
          done <- router.publish(
            userService.AddPerson(id, id)
          )
        } yield done
      }

      val names = Seq(
        "Franco",
        "Manuel",
        "Nacho",
        "Miguel"
      )

      println("Registering persons:")
      names
        .map(registerPerson)
        .foreach(println)

      val chatId = ChatId("chat-1")
      router.publish(
        chatService.Register(
          chatId,
          Chat(Set.empty[PersonId])
        )
      )

      println("Adding persons to chat:")
      names
        .map { name =>
          router.publish(
            chatService.AddPerson(
              chatId,
              name
            )
          )
        }
        .foreach(println)

      def sendMessage(name: PersonId, chatId: ChatId, message: String) =
        router.publish(
          chatService.SendMessage(
            name,
            chatId,
            Message("message-id", name.id, message)
          )
        )

      val messages = (1 to 3) map { i => s"Hello! - ${i}" }
      messages.foreach(m => sendMessage(names.head, chatId, m))

      for {
        message1 <- router.publish(
          chatService.ReadMessage(names.head, chatId)
        )
        message2 <- router.publish(
          chatService.ReadMessage(names.head, chatId)
        )
        message3 <- router.publish(
          chatService.ReadMessage(names.head, chatId)
        )
      } yield {
        println(s"after: $message1")
        println(s"after: $message2")
        println(s"after: $message3")

        for {
          message4 <- router.publish(
            chatService.ReadMessage(names.head, chatId)
          )
        } yield {
          println(s"after: $message4")
        }
      }
      // OUTPUT

      /*
      case class ExampleRule(id: String) extends Action[Boolean]
      object ExampleRule {
        implicit def handler[F[_]: MError]: ExampleRule => F[Boolean] = { (user: ExampleRule) =>
          MError[F].pure(true)
        }
      }

      import arch.infra.router.{ Action, RouterF }
      case class ReadMessages(id: String) extends Action[Seq[String]]
      object ReadMessages {
        implicit def handler[F[_]: MError: RouterF]: ReadMessages => F[Boolean] = { (user: ReadMessages) =>

          def when[F[_], E](e: => Action[Boolean])(b: MError[E]) =
            MError[F].ifM(
              RouterF[F].publish(
                e
              )
            )(
              b,
              MError[F].raiseError(null)
            )
          when(ExampleRule(action.userId))(MError[F] pure Seq("messages"))
        }
      } */

    }
    println(result)

  }
}
