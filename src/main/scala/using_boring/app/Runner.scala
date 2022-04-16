package using_boring.app

import arch.common.Program.ProgramError
import cats.effect.unsafe.IORuntime
import com.typesafe.config.{Config, ConfigFactory}
import using_boring.domain.user.model.User
import using_boring.domain.user.service.find.FindUserAction
import using_boring.domain.user.service.register
import using_boring.domain.user.service.register.RegisterUserAction
import using_boring.infrastructure.{ProgramLive}

object Runner {
  import ProgramBuilder._

  import scala.jdk.CollectionConverters._

  val prod = false
  val config: Config = ConfigFactory.parseMap(
    Map(
      "user.flag" -> true,
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
      val actionResult = router.publish[Option[User], FindUserAction](
        FindUserAction("Prom3th3us")
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
        before <- router.publish[Option[User], FindUserAction](
          FindUserAction("Prom3th3us")
        )
        _ <- router.publish[Either[ProgramError, Unit], RegisterUserAction](
          register.RegisterUserAction("Prom3th3us")
        )
        after <- router.publish[Option[User], FindUserAction](
          FindUserAction("Prom3th3us")
        )
      } yield {
        println("HERE! 1")
        println(s"before: ${before}")
        println(s"after: $after")
      }
      // OUTPUT

    }
    println(result)

  }
}
