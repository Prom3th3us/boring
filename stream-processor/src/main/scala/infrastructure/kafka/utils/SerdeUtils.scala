package infrastructure.kafka.utils

import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.streams.scala.serialization.Serdes
import play.api.libs.json.{Format, JsError, JsSuccess, Json}

import java.nio.charset.StandardCharsets

object SerdeUtils {
  implicit def serde[A >: Null: Format]: Serde[A] = {
    val serializer = (a: A) => {
      Json.prettyPrint(Json.toJson(a)).getBytes
    }
    val deserializer = (aAsBytes: Array[Byte]) => {
      val aAsString = new String(aAsBytes, StandardCharsets.UTF_8)
      Json.fromJson(Json.parse(aAsBytes)) match {
        case JsSuccess(value, path) =>
          Option(value)
        case JsError(errors) =>
          println(
            s"There was an error converting the message $errors, the original message was: $aAsString"
          )
          Option.empty
      }
    }
    Serdes.fromFn[A](serializer, deserializer)
  }

}
