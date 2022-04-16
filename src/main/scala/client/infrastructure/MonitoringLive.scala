package client.infrastructure

import arch.infra.monitoring.MonitoringF
import client.infrastructure.ProgramLive._

object MonitoringLive {
  object MonitoringApp extends MonitoringF[App]
  object MonitoringTest extends MonitoringF[Test]
}
