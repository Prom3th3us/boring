val scala3Version = "3.1.2"

lazy val root = project
  .in(file("."))
  .settings(
    name := "pacman",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    libraryDependencies += "org.scalameta" %% "munit" % "0.7.29" % Test,
    libraryDependencies += "io.circe" %% "circe-core" % "0.15.0-M1",
    libraryDependencies += "io.circe" %% "circe-generic" % "0.15.0-M1",
    libraryDependencies += "org.typelevel" %% "cats-effect" % "3.4-389-3862cf0",
    libraryDependencies += "com.typesafe" % "config" % "1.4.2",
    libraryDependencies += "org.slf4j" % "slf4j-api" % "2.0.0-alpha7"
  )
