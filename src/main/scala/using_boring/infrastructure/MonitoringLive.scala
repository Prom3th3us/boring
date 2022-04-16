package using_boring.infrastructure

import arch.infra.monitoring.MonitoringF
import using_boring.infrastructure.ProgramLive._

object MonitoringLive {
  object MonitoringApp extends MonitoringF[App]
  object MonitoringTest extends MonitoringF[Test]
}
