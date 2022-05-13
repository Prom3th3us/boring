package infrastructure.kafka

import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.serialization.Serdes
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig, Topology}

import java.util.Properties

object Setup {
  def apply(givenAplication: StreamsBuilder => Unit): Unit = {
    implicit val kafkaProps: Properties = {
      val props = new Properties
      props.put(StreamsConfig.APPLICATION_ID_CONFIG, "orders-application")
      props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
      props.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, "exactly_once")
      props.put(
        StreamsConfig.DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG,
        "org.apache.kafka.streams.errors.LogAndContinueExceptionHandler"
      )
      props.put(
        StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG,
        Serdes.stringSerde.getClass
      )
      props
    }

    implicit val builder: StreamsBuilder = new StreamsBuilder
    givenAplication(builder)
    val topology: Topology = builder.build()
    println(topology.describe())
    val application: KafkaStreams = new KafkaStreams(topology, kafkaProps)
    application.start()
  }
}
