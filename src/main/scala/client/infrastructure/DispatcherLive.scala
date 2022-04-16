package client.infrastructure

import arch.infra.router.DispatcherF
import client.infrastructure.ProgramLive._

object DispatcherLive {
  object DispatcherApp extends DispatcherF[App]
  object DispatcherTest extends DispatcherF[Test]
}
