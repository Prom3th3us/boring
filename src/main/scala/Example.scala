object Example:
  trait Repositorio[F[_], Key, Value] {

    def get: Key => F[Option[Value]]
    def set: (Key, Value) => F[Unit]

  }
