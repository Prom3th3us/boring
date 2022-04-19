package client.domain.person.model

import arch.common.HasId
import client.domain.user.model.User.UserId

case class Person(name: String)

object Person:

  case class PersonId(id: String) extends HasId[String]
