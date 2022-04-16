package client.infrastructure

import arch.infra.json.JsonLibraryF
import client.infrastructure.ProgramLive._

object JsonLibraryLive {
  object JsonLibraryApp extends JsonLibraryF[App]
  object JsonLibraryTest extends JsonLibraryF[Test]
}
