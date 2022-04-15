package arch.infra.router

import scala.reflect.ClassTag

trait Router[F[_]] {
  def subscribe[O, A <: Action[O]: ClassTag](
      handler: ActionHandler[F, O, A]
  ): Unit
  def publish[O, A <: Action[O]](action: A): F[O]
}
