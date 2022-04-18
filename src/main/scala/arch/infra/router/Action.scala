package arch.infra.router

trait Action[R] {
  type Output = R
}

object Action {
  trait Handler[F[_], A <: Action[_]] {
    def handle(action: A): F[action.Output]
  }
}
