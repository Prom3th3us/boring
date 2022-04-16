package arch.infra.logging

import arch.common.Program.Context

trait LoggingLibrary[F[_]] {
  def logDebug(msg: String): Context => F[Unit]
  def logError(msg: String): Context => F[Unit]
  def logInfo(msg: String): Context => F[Unit]
  def logWarn(msg: String): Context => F[Unit]
}
