val scala3Version = "3.1.2"

lazy val root = project
  .in(file("."))
  .settings(
    name         := "boring",
    version      := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    resolvers += "jitpack" at "https://jitpack.io",
    libraryDependencies += "com.github.Prom3th3us" % "boring" % "develop-SNAPSHOT"
  )
