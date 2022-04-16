package client.app

import arch.common.Program.ProgramError
import arch.infra.logging.LoggingLibrary
import arch.infra.router.RouterF
import com.typesafe.config.Config
import client.domain.user.model.User
import client.domain.user.service.find.FindUserAction
import client.domain.user.service.register.RegisterUserAction

object ProgramBuilder {

  trait ProgramBuilder[F[_]] {
    def buildApp(config: Config): RouterF[F]
  }

  import client.domain.user.UserRepoLive._
  import client.infrastructure.LoggingLive._
  import client.infrastructure.ProgramLive._
  import client.infrastructure.RouterLive._

  implicit lazy val production: ProgramBuilder[App] = (config: Config) => {
    println(s"config = $config")
    implicit lazy val userRepoLive: UserRepoF[App] = UserRepoApp
    // implicit lazy val personRepoLive: PersonRepoF[App] = PersonRepoApp
    // implicit lazy val jsonLibraryLive: JsonLibraryF[App] = JsonLibraryApp
    // implicit lazy val monitoring: MonitoringLibrary[App] = MonitoringApp
    // implicit lazy val userConfigF: UserConfigF[App] = UserConfigApp
    implicit lazy val logger: LoggingLibrary[App] = LoggingApp

    implicit lazy val router: RouterF[App] = new RouterApp()
    router.subscribe(FindUserAction.handler)
    router.subscribe(
      RegisterUserAction.handler
    )
    router
  }

  implicit lazy val testing: ProgramBuilder[Test] = (config: Config) => {
    println(s"config = $config")
    implicit lazy val userRepoLive: UserRepoF[Test] = UserRepoTest
    // implicit lazy val personRepoLive: PersonRepoF[Test] = PersonRepoTest
    // implicit lazy val jsonLibraryLive: JsonLibraryF[Test] = JsonLibraryTest
    // implicit lazy val monitoring: MonitoringLibrary[Test] = MonitoringTest
    // implicit lazy val userConfigF: UserConfigF[Test] = UserConfigTest
    implicit lazy val logger: LoggingLibrary[Test] = LoggingTest
    // implicit lazy val pacman: PacmanFF[Test]       = PacmanLive.PacmanTest
    // implicit user service extends interface
    implicit lazy val router: RouterF[Test] = new RouterTest()
    router.subscribe(FindUserAction.handler)
    router.subscribe(
      RegisterUserAction.handler
    )
    router
  }
}
