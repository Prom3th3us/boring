lazy val root = (project in file("."))
  .settings(
    organization := "actor",
    name         := "Pacman",
    version      := "0.1.0-SNAPSHOT",
    scalaVersion := "2.13.8"
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
