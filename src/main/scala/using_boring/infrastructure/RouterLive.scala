package using_boring.infrastructure

import arch.infra.logging.LoggingLibrary
import arch.infra.router.RouterF
import using_boring.infrastructure.ProgramLive._

object RouterLive {
  class RouterApp(implicit logger: LoggingLibrary[App]) extends RouterF[App]
  class RouterTest(implicit logger: LoggingLibrary[Test]) extends RouterF[Test]
}
