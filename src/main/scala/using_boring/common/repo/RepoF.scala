package using_boring.common.repo

import arch.common.Program
import arch.common.Program.{Context, MError}

// IMPL class on top Map
abstract class RepoF[
    F[_]: MError,
    Key,
    Entity
](context: Context)
    extends Repo[F, Key, Entity] {

  private var entities: Map[Key, Entity] =
    Map.empty[Key, Entity] // TODO pas as parameter

  override def set(id: Key, entity: Entity): F[Unit] =
    Program.App[F, Unit](context) {
      entities = entities.updated(id, entity)
    }

  override def get(id: Key): F[Option[Entity]] =
    Program.App[F, Option[Entity]](context) {
      entities.get(id)
    }
}
