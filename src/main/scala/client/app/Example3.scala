package client.app

import arch.common.Program.MError
import arch.infra.repo.Repo
import arch.infra.router.{ Action, RouterF }
import client.domain.user.service.find.FindUserAction

import arch.infra.router.Action
import client.domain.user.UserRepoLive.UserRepoF
import client.domain.user.model.User
import client.infrastructure.ProgramLive.{ Test }

object Example3 /* extends App {

  language.find[Test, String, User, UserRepoF[Test]]("user A")

  case object language:

    case class find[F[_], Key, Value, Rep <: Repo[F, Key, Value]](key: Key)

}
 */
