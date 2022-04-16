package arch.infra.router

import arch.common.Program.{Context, MError, ProgramError}
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
  private val context: Context = Context("router")
  private val actionNotFoundErrorCode = 1

  private val handlers: mutable.HashMap[Class[_], Action[_] => F[Any]] =
    mutable.HashMap.empty

  override def publish[O, A <: Action[O]](action: A): F[O] =
    handlers
      .get(action.getClass) match {
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
      handler: A => F[O]
  ): Unit = {
    val classTag = implicitly[ClassTag[A]]
    if (handlers.contains(classTag.runtimeClass)) {
      logger.logWarn("handler already subscribed")(
        context.copy(
          metadata =
            context.metadata + ("handler_name" -> handler.getClass.getSimpleName)
        )
      )
      ()
    } else {
      val transformed: Action[_] => F[Any] = (t: Action[_]) =>
        MError[F].map(handler(t.asInstanceOf[A]))(_.asInstanceOf[Any])
      handlers.addOne((classTag.runtimeClass -> transformed))
    }
  }

  private def handleAction[O, A <: Action[O]](
      action: A,
      handler: A => F[Any]
  ): F[O] = {
    val before = System.currentTimeMillis()
    val maybeResponse: F[O] =
      MError[F].map(handler(action))(_.asInstanceOf[O])
    val recoverable = MError[F].recoverWith(maybeResponse) {
      case error: ProgramError =>
        onFailure(error)
        recordLatencyInMillis(
          action.getClass.getSimpleName,
          before,
          System.currentTimeMillis()
        )
        maybeResponse
    }
    MError[F].map(recoverable) { result =>
      onSuccess(action.getClass.getSimpleName)
      recordLatencyInMillis(
        action.getClass.getSimpleName,
        before,
        System.currentTimeMillis()
      )
      result
    }
  }
}

object RouterF {
  def apply[F[_]](implicit router: RouterF[F]): RouterF[F] = router
}
