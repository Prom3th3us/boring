package infrastructure.kafka.utils

import org.apache.kafka.streams.kstream.Named
import org.apache.kafka.streams.scala.kstream.{ Branched, Joined, KStream, KTable }

object KafkaStreamsUtils {
  implicit class KStreamEitherUtils[Key, Failure, Success](
      a: KStream[Key, Either[Failure, Success]]
  ) {
    def splitEither(splitName: String)(
        leftTopicName: String,
        rightTopicName: String
    ): (KStream[Key, Failure], KStream[Key, Success]) = {
      val branches = a
        .split(Named.as(s"$splitName-"))
        .branch((key, either) => either.isLeft, Branched.as(leftTopicName))
        .branch((key, either) => either.isRight, Branched.as(rightTopicName))
        .noDefaultBranch()
      (
        branches(s"$splitName-$leftTopicName")
          .mapValues(
            (k, v) => v.left.get,
            Named.as(s"$splitName-$leftTopicName.left")
          ),
        branches(s"$splitName-$rightTopicName")
          .mapValues(
            (k, v) => v.right.get,
            Named.as(s"$splitName-$rightTopicName.right")
          )
      )
    }

  }

  implicit class KTableDefaultValue[K, V](a: KStream[K, V]) {
    def leftJoinWithDefault[VT, VR](default: VT)(table: KTable[K, VT])(
        joiner: (V, VT) => VR
    )(implicit joined: Joined[K, V, VT]): KStream[K, VR] =
      a.leftJoin(table)((a, table) =>
        Option(table) match {
          case Some(table) => joiner(a, table)
          case None        => joiner(a, default)
        }
      )

  }
}
