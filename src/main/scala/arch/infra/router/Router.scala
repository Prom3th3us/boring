package arch.infra.router

import scala.reflect.ClassTag

trait Router[F[_]] {
  def subscribe[O, A <: Action[O]: ClassTag](
      handler: Action.Handler[F, A]
  ): F[Unit]
  def publish[O, A <: Action[O]](action: A): F[action.Output]
}

object Router:
  implicit def subscribe[F[_], r <: Router[F], O, A <: Action[O]: ClassTag](using rr: r)(
      handler: Action.Handler[F, A]
  ): F[Unit] =
    rr.subscribe(handler)

  implicit def publish[F[_], r <: Router[F], O, A <: Action[O]: ClassTag](using rr: r)(action: A): F[O] =
    rr.publish(action)
