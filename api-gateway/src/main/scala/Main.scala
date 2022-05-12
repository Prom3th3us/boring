import zhttp.http._
import zhttp.service.Server
import zhttp.socket.{ Socket, SocketApp, SocketDecoder, SocketProtocol, WebSocketFrame }
import zio.stream.ZStream
import zio._
import zhttp.service.{ ChannelFactory, Client, EventLoopGroup }

import java.util.concurrent.atomic.AtomicInteger
import scala.concurrent.{ ExecutionContext, Future }
import scala.language.postfixOps

object Main extends zio.ZIOAppDefault {

  sealed trait Protocol
  object Protocol {
    case class Messages(messages: Seq[String]) extends Protocol
    case class ConnectionClosed()              extends Protocol
  }

  var acc = 0
  def GET_USER_MESSAGES(implicit ec: ExecutionContext): Future[Protocol] = {
    acc = acc + 1
    val index = acc
    Future.successful {
      if (index % 3 == 0) Protocol.ConnectionClosed()
      else
        Protocol.Messages(Seq("message1", "message2", "message3"))
    }
  }

  def getMessages: Task[Protocol] =
    ZIO.fromFuture(implicit ec => GET_USER_MESSAGES)

  // Message Handlers
  private val open = Socket.succeed(WebSocketFrame.text("Greetings!"))

  private val echo = Socket.collect[WebSocketFrame] { case WebSocketFrame.Text(text) =>
    ZStream
      .repeatZIO(getMessages)
      .schedule(Schedule.spaced(1 second))
      .map {
        case Protocol.ConnectionClosed() =>
          WebSocketFrame.close(1000)
        case Protocol.Messages(e) =>
          println(s"Repeating $e to the websocket connection")
          WebSocketFrame.text(e.toString)
      }
  }

  private val pingPong = Socket.collect[WebSocketFrame] {
    case WebSocketFrame.Ping => ZStream.succeed(WebSocketFrame.pong)
    case WebSocketFrame.Pong => ZStream.succeed(WebSocketFrame.ping)
  }

  // Setup protocol settings
  private val protocol = SocketProtocol.subProtocol("json")

  // Setup decoder settings
  private val decoder = SocketDecoder.allowExtensions

  // Combine all channel handlers together
  private val socketApp = {

    SocketApp(echo merge pingPong) // Called after each message being received on the channel

      // Called after the request is successfully upgraded to websocket
      .onOpen(open)

      // Called after the connection is closed
      .onClose { e =>
        println(s"Closed! ${e}")
        Console.printLine("Closed!").ignore
      }

      // Called whenever there is an error on the socket channel
      .onError { e =>
        println(s"Error! ${e}")
        Console.printLine("Error!").ignore
      }

    // Setup websocket decoder config
    // .withDecoder(decoder)

    // Setup websocket protocol config
    // .withProtocol(protocol)
  }

  private val app =
    Http.collectZIO[Request] {
      case Method.GET -> !! / "greet" / name  => ZIO succeed Response.text(s"Greetings {$name}!")
      case Method.GET -> !! / "subscriptions" => socketApp.toResponse // si el usuario existe y no esta conectado
    }

  override def run: ZIO[Any with ZIOAppArgs with Scope, Nothing, Any] =
    Server.start(8090, app).exitCode

}
