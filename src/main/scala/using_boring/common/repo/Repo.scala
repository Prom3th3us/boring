package using_boring.common.repo

trait Repo[F[_], Key, Entity] {
  def set(id: Key, a: Entity): F[Unit]

  def get(id: Key): F[Option[Entity]]
}
