package arch.model

import arch.common.ProgramLive.{App, Test}

object UserRepoLive extends UserRepoF[App]
object UserRepoTest extends UserRepoF[Test]

