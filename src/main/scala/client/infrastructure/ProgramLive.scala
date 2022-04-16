package client.infrastructure

import arch.common.Program.ProgramError
import cats.data.EitherT
import cats.effect.IO

object ProgramLive {
  // each type should be a subtype of MonadError
  type App[A] = EitherT[IO, ProgramError, A]
  type Test[A] = Either[ProgramError, A]
}
