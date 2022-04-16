package arch.infra.config

import arch.common.Program.{Context, MError, ProgramError}
import com.typesafe.config.Config

import scala.util.{Failure, Success, Try}

trait ConfigF[A, F[_]] {
  def fromConfig(config: Config): F[A]
}

object ConfigF {
  val ctx: Context = Context("config_parser")

  def parse[A, F[_]: MError](cfg: Config)(fn: Config => A): F[A] = {
    val either = Try(fn(cfg)) match {
      case Failure(exception) =>
        Left(ProgramError(exception, exception.getMessage, ctx))
      case Success(value) => Right(value)
    }
    MError[F].fromEither(either)
  }

  def parseOpt[A, F[_]: MError](cfg: Config)(fn: Config => A): F[Option[A]] = {
    MError[F].pure(Try(fn(cfg)).toOption)
  }
}
