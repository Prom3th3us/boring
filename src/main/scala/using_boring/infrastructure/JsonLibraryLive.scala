package using_boring.infrastructure

import arch.infra.json.JsonLibraryF
import using_boring.infrastructure.ProgramLive._

object JsonLibraryLive {
  object JsonLibraryApp extends JsonLibraryF[App]
  object JsonLibraryTest extends JsonLibraryF[Test]
}
