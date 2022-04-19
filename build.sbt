lazy val shared = project
  .settings(scalaVersion := "3.1.2")

resolvers += "jitpack" at "https://jitpack.io"
lazy val root = (project in file("."))
  .dependsOn(shared)
  .settings(scalacOptions += "-Ytasty-reader")
  .settings(
    organization := "com.example",
    name         := "Pacman",
    version      := "0.1.0-SNAPSHOT",
    scalaVersion := "2.13.8"
  )
  .settings(
    libraryDependencies += "com.github.Prom3th3us" % "boring"    % "develop-SNAPSHOT",
    libraryDependencies += "org.slf4j"             % "slf4j-api" % "2.0.0-alpha7"
  )

enablePlugins(AkkaserverlessPlugin, JavaAppPackaging, DockerPlugin)
dockerBaseImage             := "docker.io/library/adoptopenjdk:11-jre-hotspot"
dockerUsername              := sys.props.get("docker.username")
dockerRepository            := sys.props.get("docker.registry")
dockerUpdateLatest          := true
ThisBuild / dynverSeparator := "-"
run / fork                  := true
run / envVars += ("HOST", "0.0.0.0")

Compile / scalacOptions ++= Seq(
  "-target:11",
  "-deprecation",
  "-feature",
  "-unchecked",
  "-Xlog-reflective-calls",
  "-Xlint"
)
Compile / javacOptions ++= Seq(
  "-Xlint:unchecked",
  "-Xlint:deprecation",
  "-parameters" // for Jackson
)

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.2.7" % Test
)
