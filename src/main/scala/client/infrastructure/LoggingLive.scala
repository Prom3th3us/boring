package client.infrastructure

import arch.infra.logging.LoggingF
import client.infrastructure.ProgramLive._

object LoggingLive {
  object LoggingApp extends LoggingF[App]
  object LoggingTest extends LoggingF[Test]
}
