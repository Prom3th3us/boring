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
