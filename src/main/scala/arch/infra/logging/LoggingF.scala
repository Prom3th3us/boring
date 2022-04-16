package arch.infra.logging

import arch.common.Program
import arch.common.Program.MError
import org.slf4j.LoggerFactory

// TODO fix why ctx is not being logged
case class LoggingF[F[_]: MError]() extends LoggingLibrary[F] {
  private val logger = LoggerFactory.getLogger("main-logger")
  override def logDebug(msg: String): Program.Context => F[Unit] =
    ctx => MError[F].pure(logger.debug(msg, ctx))

  override def logError(msg: String): Program.Context => F[Unit] =
    ctx => MError[F].pure(logger.error(msg, ctx))

  override def logInfo(msg: String): Program.Context => F[Unit] =
    ctx => MError[F].pure(logger.info(msg, ctx))

  override def logWarn(msg: String): Program.Context => F[Unit] =
    ctx => MError[F].pure(logger.warn(msg, ctx))
}
