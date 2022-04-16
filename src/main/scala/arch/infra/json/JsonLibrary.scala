package arch.infra.json

trait JsonLibrary[F[_]] {
  type JsonType
  trait JsonPrinter {
    def prettyPrint(json: JsonType): String
  }
  trait JsonTo[A] {
    def to(json: JsonType): F[A]
  }
  trait JsonFrom[A] {
    def from(from: A): JsonType
  }
  trait JsonFromTo[A] extends JsonTo[A] with JsonFrom[A]
  trait JsonParser {
    def parse(str: String): F[JsonType]
  }
}
