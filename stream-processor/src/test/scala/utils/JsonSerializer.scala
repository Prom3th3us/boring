package utils

import org.apache.kafka.common.serialization.{ Deserializer, Serializer }
import play.api.libs.json.{ Format, Json }

import java.nio.charset.StandardCharsets
import java.util
import scala.language.implicitConversions

object JsonSerializer {

  case class JsonSerializer[A: Format]() extends Serializer[A] with Deserializer[A] {
    s: Serializer[A] with Deserializer[A] =>

    implicit def toJson(a: A): String =
      Json.prettyPrint(Json.toJson(a))

    implicit def fromJson(a: String): A =
      Json.fromJson[A](Json.parse(a)).get

    override def serialize(topic: String, data: A): Array[Byte] =
      toJson(data).getBytes

    override def deserialize(topic: String, data: Array[Byte]): A = fromJson(
      new String(data, StandardCharsets.UTF_8)
    )

    override def configure(
        configs: util.Map[String, _],
        isKey: Boolean
    ): Unit = {
      super[Serializer].configure(configs, isKey)
      super[Deserializer].configure(configs, isKey)
    }

  }

}
