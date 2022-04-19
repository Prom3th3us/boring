package client.domain.chat

import arch.common.Program.MError
import arch.common.HasId

import scala.collection.mutable

class PacmanF[
    F[_]: MError,
    Id,
    Message <: HasId[Id],
    Receiver <: HasId[Id]
](
    messagesPerUser: mutable.HashMap[Receiver, mutable.Queue[Message]]
) extends Pacman[F, Id, Message, Receiver] {

  type UserId    = HasId[Id]
  type MessageId = HasId[Id]

  override def readMessagesForMe(
      whoIAm: Receiver,
      lastN: Int
  ): F[Seq[Message]] = {
    println("messagesPerUser: \n  " + messagesPerUser.map(e => e._1 -> e._2.size).toSeq.mkString("\n  "))
    MError[F] pure (for {
      userHasMessages <- messagesPerUser.get(whoIAm)
    } yield {
      val messagesRead = lastN match {
        case n if n == -1 =>
          userHasMessages.dequeueAll(_ => true)
        case n =>
          var i = 0
          userHasMessages.dequeueAll { _ =>
            i += 1
            i == n
          }
      }
      messagesRead
    }).fold(Seq.empty[Message])(identity)
  }

  override def add(message: Message, receivers: Set[Receiver]): F[Unit] = {
    MError[F] pure (receivers foreach { userId =>
      messagesPerUser.get(userId) match {
        case Some(queue) =>
          queue.enqueue(message)
        case None =>
          messagesPerUser.addOne((userId, new mutable.Queue[Message]().addOne(message)))
      }
    })
  }
}
