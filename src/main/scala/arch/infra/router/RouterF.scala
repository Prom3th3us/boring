package arch.infra.router

import arch.common.Program.{ Context, MError, ProgramError }
import arch.infra.logging.LoggingLibrary

import scala.collection.mutable
import scala.reflect.ClassTag

class RouterF[F[_]: MError](
    onSuccess: String => Unit = _ => { println("success") },
    onFailure: ProgramError => Unit = _ => { println("failure") },
    recordLatencyInMillis: (String, Long, Long) => Unit = (_, _, _) => {
      println("recording latency")
    }
)(implicit logger: LoggingLibrary[F])
    extends Router[F] {
  private val context: Context        = Context("router")
  private val actionNotFoundErrorCode = 1

  private val handlers: mutable.HashMap[Class[_], Action.Handler[F, _]] =
    mutable.HashMap.empty

  private def getHandlerForAction[O, A <: Action[O]](action: A): Option[Action.Handler[F, A]] =
    handlers.get(action.getClass).map { handler =>
      handler.asInstanceOf[Action.Handler[F, A]]
    }

  private def handleAction[O, A <: Action[O]](
      action: A,
      handler: Action.Handler[F, A]
  ): F[O] = handler.handle(action)

  override def publish[O, A <: Action[O]](action: A): F[action.Output] =
    getHandlerForAction(action) match {
      case Some(handler) => handleAction(action, handler)
      case None =>
        MError[F].raiseError(
          ProgramError(
            "action not found",
            s"action ${action.getClass.getSimpleName} not found",
            context,
            actionNotFoundErrorCode
          )
        )
    }

  override def subscribe[O, A <: Action[O]: ClassTag](
      handler: Action.Handler[F, A]
  ): F[Unit] = {
    val classTag = implicitly[ClassTag[A]]
    if (handlers.contains(classTag.runtimeClass)) {
      logger.logWarn("handler already subscribed")(
        context.copy(
          metadata = context.metadata + ("handler_name" -> classTag.runtimeClass.getSimpleName)
        )
      )
    } else {
      handlers.addOne(classTag.runtimeClass -> handler)
    }
    MError[F].pure(())
  }

}

object RouterF {
  def apply[F[_]](implicit router: RouterF[F]): RouterF[F] = router
}
