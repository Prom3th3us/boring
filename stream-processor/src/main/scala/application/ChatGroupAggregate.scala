package application

import domain.chat_group.commands.ChatGroupCommands
import domain.chat_group.entities.entities._
import domain.chat_group.errors.ChatGroupErrors
import domain.chat_group.events.ChatGroupEvents
import domain.chat_group.serialization.json._
import domain.chat_group.services.ChatGroupServices.{ ChatGroupRules, ChatGroupRulesImpl }
import domain.chat_group.state.ChatGroupState
import infrastructure.kafka.event_sourcing.EventSourcingKafka
import infrastructure.kafka.utils.SerdeUtils._
import org.apache.kafka.streams.scala.StreamsBuilder

object ChatGroupAggregate {

  def apply()(implicit
      builder: StreamsBuilder
  ): Unit = {
    new EventSourcingKafka[
      ChatGroupId,
      ChatGroupCommands,
      ChatGroupEvents,
      ChatGroupState,
      ChatGroupErrors,
      ChatGroupRules[ChatGroupEvents, ChatGroupCommands]
    ]("chat-group", ChatGroupState(Set.empty), ChatGroupRulesImpl).process

  }
}
