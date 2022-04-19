package arch.common

import cats.MonadError

import scala.util.{ Failure, Success, Try }

object Program {

  val unknownErrorCode = 0

  type MError[F[_]] = MonadError[F, ProgramError]
  case class Context(name: String, metadata: Map[String, Any] = Map.empty)
  case class ProgramError(
      error: Any,
      msg: String,
      ctx: Context,
      errorCode: Int = unknownErrorCode,
      stackTrace: Seq[String] = Seq.empty
  )

  object ProgramError {

    def simple(text: String) =
      ProgramError(
        new Exception("error message: " + text),
        "error message: " + text,
        Context("dont bother"), // TODO:
        -1,
        Seq()
      )

    def fromThrowable(exception: Throwable): Context => Int => ProgramError =
      ctx =>
        errorCode => {
          val stackTrace =
            Seq.empty // exception.getStackTrace.map(_.toString).take(10)
          ProgramError(
            exception,
            "error message: " + exception.getMessage,
            ctx,
            errorCode,
            stackTrace
          )
        }
    def fromError[E](e: E): Context => Int => ProgramError = ctx =>
      errorCode => {
        ProgramError(
          e,
          "error message: " + e.toString,
          ctx,
          errorCode,
          stackTrace = Seq.empty
        )
      }
  }

  object MError {
    def apply[F[_]](implicit m: MError[F]): MError[F] = m
  }

  object App {
    def fromEither[F[_]: MError, E, A](
        ctx: Context
    )(errorCode: => Int = unknownErrorCode)(a: => Either[E, A]): F[A] =
      MError[F].fromEither[A](
        a match {
          case Left(e)      => Left(ProgramError.fromError(e)(ctx)(errorCode))
          case Right(value) => Right(value)
        }
      )
    def fromTry[F[_]: MError, A](
        ctx: Context
    )(errorCode: => Int = unknownErrorCode)(a: => Try[A]): F[A] =
      MError[F].fromEither[A](
        a match {
          case Failure(exception) =>
            Left(ProgramError.fromThrowable(exception)(ctx)(errorCode))
          case Success(value) => Right(value)
        }
      )
    def lift[F[_]: MError, A](ctx: Context)(
        errorCode: => Int = unknownErrorCode
    )(a: => A = ()): F[A] = fromTry(ctx)(errorCode)(Try(a))
    def apply[F[_]: MError, A](
        ctx: Context,
        errorCode: => Int = unknownErrorCode
    )(a: => A): F[A] = lift(ctx)(errorCode)(a)
    def unit[F[_]: MError](ctx: Context): F[Unit] = apply[F, Unit](ctx)(())
  }
}
