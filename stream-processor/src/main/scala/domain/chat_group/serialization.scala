package domain.chat_group

import domain.chat_group.commands.ChatGroupCommands
import domain.chat_group.errors.ChatGroupErrors
import domain.chat_group.events.ChatGroupEvents
import domain.chat_group.state.ChatGroupState

object serialization {
  object json {

    import io.leonard.TraitFormat.{ caseObjectFormat, traitFormat }
    import play.api.libs.json.Json.format

    implicit val userIdFormat =
      format[entities.entities.UserId]
    implicit val chatGroupIdFormat =
      format[entities.entities.ChatGroupId]
    implicit val messageFormat =
      format[entities.entities.Message]

    implicit val chatGroupCommandsFormat = {
      traitFormat[ChatGroupCommands] <<
        format[ChatGroupCommands.AddUser] <<
        format[ChatGroupCommands.DeleteUserFromChat]
      // format[ChatGroupCommands.SendMessageToChat] <<
      // format[ChatGroupCommands.SendMessageToUser]
    }

    implicit val chatGroupEventsFormat = {
      traitFormat[ChatGroupEvents] <<
        format[ChatGroupEvents.AddedUserToChat] <<
        format[ChatGroupEvents.DeletedUserFromChat]
    }

    implicit val chatGroupErrorsFormat = {
      traitFormat[ChatGroupErrors] <<
        caseObjectFormat(ChatGroupErrors.ToBeImplemented)
    }

    implicit val chatGroupStateFormat = format[ChatGroupState]

  }
}
