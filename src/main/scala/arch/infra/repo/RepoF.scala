package arch.infra.repo

import arch.common.Program
import arch.common.Program.{ Context, MError }

// IMPL class on top Map
trait RepoF[
    F[_]: MError
](context: Context)
    extends Repo[F] {

  private var entities: Map[Key, Value] =
    Map.empty[Key, Value] // TODO pas as parameter

  override def set(id: Key, Value: Value): F[Unit] =
    Program.App[F, Unit](context) {
      entities = entities.updated(id, Value)
    }

  override def get(id: Key): F[Option[Value]] =
    Program.App[F, Option[Value]](context) {
      entities.get(id)
    }
}
