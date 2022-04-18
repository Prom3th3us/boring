package client.app

import arch.common.Program.ProgramError
import cats.effect.unsafe.IORuntime
import com.typesafe.config.{ Config, ConfigFactory }
import client.domain.user.model.User
import client.domain.user.model.User._
import client.domain.user.service.find.FindUserAction
import client.domain.user.service.register
import client.domain.user.service.register.RegisterUserAction
import client.infrastructure.{ ProgramLive }
import arch.common.Program.MError

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
        FindUserAction(UserId("Prom3th3us"))
      )
      // OUTPUT
      actionResult.value.unsafeRunSync()
    } else {
      println(s"RUNNING ENV=test")
      // DEFINITIONS
      type Env[A] = ProgramLive.Test[A]
      // EXECUTION
      val router = implicitly[ProgramBuilder[Env]].buildApp(config)
      for {
        before <- router.publish(
          FindUserAction(UserId("Prom3th3us"))
        )
        _ <- router.publish(
          register.RegisterUserAction(UserId("Prom3th3us"), User("pepe"))
        )
        after <- router.publish(
          FindUserAction(UserId("Prom3th3us"))
        )
      } yield {
        println("HERE! 1")
        println(s"before: ${before}")
        println(s"after: $after")
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
