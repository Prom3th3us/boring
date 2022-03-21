name := "workshop"

version := "0.1"

scalaVersion := "2.13.6"

libraryDependencies ++= List(
  "com.softwaremill.sttp.client3" %% "async-http-client-backend-monix" % "3.3.15",
  "com.softwaremill.sttp.client3" %% "circe" % "3.3.15",
  "io.circe" %% "circe-generic" % "0.14.1"
)

libraryDependencies += "com.datastax.oss" % "java-driver-core" % "4.13.0"
