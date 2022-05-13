package domain.chat_group.services

import domain.Rules
import domain.chat_group.commands.ChatGroupCommands
import domain.chat_group.commands.ChatGroupCommands._
import domain.chat_group.errors.ChatGroupErrors
import domain.chat_group.events.ChatGroupEvents
import domain.chat_group.events.ChatGroupEvents._
import domain.chat_group.state.ChatGroupState

object ChatGroupServices {

  trait ChatGroupRules[Event <: ChatGroupEvents, Command <: ChatGroupCommands]
      extends Rules[Event, ChatGroupState, Command, ChatGroupErrors]

  case object ChatGroupRulesImpl extends ChatGroupRules[ChatGroupEvents, ChatGroupCommands] {
    override def validator(
        context: ChatGroupState
    ): ChatGroupCommands => Either[ChatGroupErrors, Seq[ChatGroupEvents]] = {
      case cmd: AddUser            => addUser.validator(context)(cmd)
      case cmd: DeleteUserFromChat => removeUser.validator(context)(cmd)
    }
  }

  object addUser extends ChatGroupRules[AddedUserToChat, AddUser] {
    override def validator(context: ChatGroupState): AddUser => Either[ChatGroupErrors, Seq[AddedUserToChat]] = {
      case AddUser(userId) =>
        Right(Seq(AddedUserToChat(userId)))
    }
  }
  object removeUser extends ChatGroupRules[DeletedUserFromChat, DeleteUserFromChat] {
    override def validator(
        context: ChatGroupState
    ): DeleteUserFromChat => Either[ChatGroupErrors, Seq[DeletedUserFromChat]] = { case DeleteUserFromChat(userId) =>
      Right(Seq(DeletedUserFromChat(userId)))
    }
  }
}
