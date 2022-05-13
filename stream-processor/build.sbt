name := "stream-processor"

version := "0.1"

scalaVersion := "2.13.7"
libraryDependencies ++= Seq(
  ("org.apache.kafka" % "kafka-streams" % "2.8.0")
    .exclude("orgs.rocksdb", "rocksdbjni"),
  "org.apache.kafka" %% "kafka-streams-scala" % "2.8.0",
  "io.leonard"       %% "play-json-traits"    % "1.5.1",
  "com.github.scopt" %% "scopt"               % "4.0.1"
)
// https://mvnrepository.com/artifact/org.slf4j/slf4j-api
libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.32"
val logback = "1.2.3"
libraryDependencies += "ch.qos.logback"          % "logback-core"        % logback
libraryDependencies += "ch.qos.logback"          % "logback-classic"     % logback
libraryDependencies += "io.github.embeddedkafka" % "embedded-kafka_2.13" % "3.0.0"
// https://mvnrepository.com/artifact/org.scalatest/scalatest
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.10" % Test
// https://mvnrepository.com/artifact/io.github.embeddedkafka/embedded-kafka-streams
libraryDependencies += "io.github.embeddedkafka" %% "embedded-kafka-streams" % "3.0.0"
// https://mvnrepository.com/artifact/org.apache.kafka/kafka-streams-test-utils
libraryDependencies += "org.apache.kafka" % "kafka-streams-test-utils" % "3.0.0" % Test

// https://mvnrepository.com/artifact/org.typelevel/cats-effect
libraryDependencies += "org.typelevel" %% "cats-effect" % "3.3.0"
// https://mvnrepository.com/artifact/org.typelevel/cats-core
libraryDependencies += "org.typelevel" %% "cats-core" % "2.7.0"

// https://mvnrepository.com/artifact/org.rocksdb/rocksdbjni
dependencyOverrides += "org.rocksdb" % "rocksdbjni" % "7.2.2" % Test
