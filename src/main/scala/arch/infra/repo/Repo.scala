package arch.infra.repo

trait Repo[F[_]] {
  type Key
  type Value

  def set(id: Key, a: Value): F[Unit]

  def get(id: Key): F[Option[Value]]

}

object Repo:

  implicit def find[F[_], r <: Repo[F]](using rr: r)(key: rr.Key): F[Option[rr.Value]] =
    rr.get(key)

  implicit def set[F[_], r <: Repo[F]](using rr: r)(key: rr.Key, value: rr.Value): F[Unit] =
    rr.set(key, value)
