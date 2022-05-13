organization := "actor"
name         := "boring"
version      := "0.1.0-SNAPSHOT"
scalaVersion := "2.13.8"

lazy val `akka-serverless`  = project
lazy val `api-gateway`      = project.dependsOn(`akka-serverless`)
lazy val `stream-processor` = project
