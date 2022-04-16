package client.domain.user.model

import arch.common.HasId
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}

case class User(id: String) extends HasId[String]

object User {
  implicit val userDecoder: Decoder[User] = deriveDecoder[User]
  implicit val userEncoder: Encoder[User] = deriveEncoder[User]
}
