package arch.infra.json

import util.Try
import arch.common.Program
import arch.common.Program.{Context, MError}
import io.circe.{Decoder, Encoder, Json, Printer}

// IMPL class on top of circe
class JsonLibraryF[F[_]: MError] extends JsonLibrary[F] {
  type JsonType = Json

  val ctx: Context = Context("json")
  val jsonDecodeErrorCode = 1
  val jsonParseErrorCode = 2

  class To[A](implicit d: Decoder[A]) extends JsonTo[A] {
    override def to(json: JsonType): F[A] =
      Program.App.fromEither[F, Throwable, A](ctx)(jsonDecodeErrorCode)(
        d.decodeJson(json)
      )
  }
  class From[A](implicit e: Encoder[A]) extends JsonFrom[A] {
    override def from(from: A): JsonType = e.apply(from)
  }
  class JsonFromToF[A](t: To[A], f: From[A]) extends JsonFromTo[A] {
    override def to(json: JsonType): F[A] = t.to(json)
    override def from(from: A): JsonType = f.from(from)
  }
  object PrettyPrinter extends JsonPrinter {
    override def prettyPrint(json: Json): String = Printer.spaces2.print(json)
  }
  object JsonParserF extends JsonParser {
    override def parse(str: String): F[Json] =
      Program.App.fromEither[F, Throwable, Json](ctx)(jsonParseErrorCode)(
        Try(Json.fromString(str)).toEither
      )
  }

  def jsonFromTo[A](implicit d: Decoder[A], e: Encoder[A]): JsonFromTo[A] =
    new JsonFromToF[A](new To(), new From())
  def prettyPrint(json: Json): String = PrettyPrinter.prettyPrint(json)
  def parse(json: String): F[Json] = JsonParserF.parse(json)
}
