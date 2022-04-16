package using_boring.infrastructure

import arch.infra.router.DispatcherF
import using_boring.infrastructure.ProgramLive._

object DispatcherLive {
  object DispatcherApp extends DispatcherF[App]
  object DispatcherTest extends DispatcherF[Test]
}
