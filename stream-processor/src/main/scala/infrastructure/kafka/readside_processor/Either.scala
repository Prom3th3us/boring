package infrastructure.kafka.readside_processor

import infrastructure.kafka.readside_processor.ReadsideProcessor.Composable
import infrastructure.kafka.utils.KafkaStreamsUtils.KStreamEitherUtils
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.kstream.{ Consumed, KStream }

class Either[Key, Value](
    topic: String,
    predicate: (Key, Value) => Boolean,
    processorName: String,
    leftTopicName: String,
    rightTopicName: String
)(implicit
    builder: StreamsBuilder,
    keySerde: Serde[Key],
    valueSerde: Serde[Value]
) extends Composable[(KStream[Key, Value], KStream[Key, Value])] {

  def process =
    builder
      .stream[Key, Value](topic)(
        Consumed
          .`with`[Key, Value]
          .withName(s"$topic-readBy-$processorName")
      )
      .map { (k, v) =>
        if (predicate(k, v)) (k, Right(v))
        else (k, Left(v))
      }
      .splitEither(s"$topic-$processorName.split")(
        leftTopicName,
        rightTopicName
      )

}
