package application

import domain.chat_group.entities.entities
import domain.chat_group.entities.entities.{ ChatGroupId, Message, UserId }
import domain.chat_group.state.ChatGroupState
import org.apache.kafka.streams.scala.StreamsBuilder

object BroadcastMessage {

  def apply()(implicit builder: StreamsBuilder) = {

    import domain.chat_group.serialization.json._
    import infrastructure.kafka.utils.SerdeUtils.serde
    import org.apache.kafka.streams.scala.kstream._

    val messages = builder.stream("messages")(
      Consumed.`with`[ChatGroupId, Message].withName("messages-in")
    )

    val snapshots = builder.table("chat-group-snapshots")(
      Consumed.`with`[ChatGroupId, ChatGroupState].withName("chat-group-snapshots")
    )

    val `user-messages`: KStream[entities.UserId, Message] =
      messages
        .join(table = snapshots)(joiner = { case (message, chatGroupState) =>
          println(s"Joining chatGroupState with message: ${message} ${chatGroupState}")
          chatGroupState.users.map(user => user -> message)
        })(
          Joined
            .`with`[ChatGroupId, Message, ChatGroupState]
            .withName(
              s"messages-join-chat-state"
            )
        )
        .flatMap { case (chatId, usersAndMessages) =>
          println(s"chatId $chatId usersAndMessages $usersAndMessages")
          usersAndMessages.toSeq
        }

    `user-messages`.to("user-messages")(
      Produced
        .`with`[UserId, Message]
        .withName(
          s"user-messages-out"
        )
    )
  }
}
