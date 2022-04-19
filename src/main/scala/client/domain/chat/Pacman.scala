package client.domain.chat

import arch.common.HasId
import cats.implicits._
import arch.common.Program.MError

trait Pacman[
    F[_]: MError,
    Id,
    Message <: HasId[Id],
    Receiver
] {
  def add(
      message: Message,
      receivers: Set[Receiver]
  ): F[Unit]

  def readMessagesForMe(
      whoIAm: Receiver,
      lastN: Int = -1
  ): F[Seq[Message]]

  def readLastMessage(
      whoIAm: Receiver,
      lastN: Int = -1
  ): F[Option[Message]] =
    for {
      messages <- readMessagesForMe(whoIAm, 1)
    } yield messages.headOption

}
