package arch.infra.router

trait DispatcherF[F[_]] {
  def dispatch[O, A <: Action[O]](
      a: A
  )(implicit fn: A => F[O]): F[O] = {
    fn(a)
  }
}

object DispatcherF {
  def apply[F[_]](implicit dispatcherF: DispatcherF[F]): DispatcherF[F] =
    dispatcherF
}
