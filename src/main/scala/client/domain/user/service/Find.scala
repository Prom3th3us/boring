package client.domain.user.service

import arch.common.Program.{ Context, MError, ProgramError }
import arch.infra.repo.Repo.*
import arch.infra.router.Action
import cats.data.OptionT
import cats.implicits.*
import cats.instances.*
import client.domain.person.PersonRepoLive.PersonRepoF
import client.domain.person.model.Person
import client.domain.person.model.Person.*
import client.domain.user.UserRepoLive.UserRepoF
import client.domain.user.model.User
import client.domain.user.model.User.*
import client.domain.user.{ service => userService }
case class Find(id: UserId) extends Action[User]

object Find {
  implicit def handler[F[_]: MError: UserRepoF]: Action.Handler[F, Find] =
    action =>
      for {
        result <- find(action.id)
        user   <- MError[F].fromOption(result, ProgramError.simple("user not found"))
      } yield user
}
