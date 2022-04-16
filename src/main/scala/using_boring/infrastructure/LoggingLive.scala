package using_boring.infrastructure

import arch.infra.logging.LoggingF
import using_boring.infrastructure.ProgramLive._

object LoggingLive {
  object LoggingApp extends LoggingF[App]
  object LoggingTest extends LoggingF[Test]
}
