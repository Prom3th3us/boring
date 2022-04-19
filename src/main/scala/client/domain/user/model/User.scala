package client.domain.user.model

import arch.common.HasId
import client.domain.person.model.Person.PersonId

case class User(name: String, persons: Set[PersonId])

object User {
  case class UserId(id: String) extends HasId[String]
}
