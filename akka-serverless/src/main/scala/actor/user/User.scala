package actor.user

import actor.person.domain.PersonCreated
import actor.user
import actor.user.domain._
import com.akkaserverless.scalasdk.eventsourcedentity.EventSourcedEntity
import com.akkaserverless.scalasdk.eventsourcedentity.EventSourcedEntityContext
import com.google.protobuf.empty.Empty
import actor.messaging.domain.SendMessageDtoM
// This class was initially generated based on the .proto definition by Akka Serverless tooling.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

class User(context: EventSourcedEntityContext) extends AbstractUser {
  override def emptyState: UserState = UserState()

  override def receiveMessage(
      currentState: UserState,
      sendMessageDto: SendMessageDto
  ): EventSourcedEntity.Effect[Empty] = {
    println(s"[DEBUG] User.receiveMessage ${sendMessageDto.userId} received ${sendMessageDto.message}")
    effects
      .emitEvent(
        ReceiveMessageDto(
          userId = sendMessageDto.userId,
          message = sendMessageDto.message
        )
      )
      .thenReply(_ => Empty())
  }

  override def receiveMessageDto(currentState: UserState, receiveMessageDto: ReceiveMessageDto): UserState = {
    println(s"[DEBUG] User.receiveMessageDto ${receiveMessageDto.userId} received ${receiveMessageDto.message}")
    currentState.copy(
      messages = currentState.messages appended receiveMessageDto.message
    )
  }

  override def createUser(
      currentState: UserState,
      createUserCommand: CreateUserCommand
  ): EventSourcedEntity.Effect[Empty] =
    effects
      .emitEvent(
        UserCreated(
          userId = createUserCommand.userId,
          name = createUserCommand.name
        )
      )
      .thenReply(state => Empty())

  override def getUser(currentState: UserState, getUserCommand: GetUserCommand): EventSourcedEntity.Effect[UserView] =
    effects.reply(
      UserView(
        name = currentState.name,
        persons = currentState.persons,
        messages = currentState.messages
      )
    )

  override def userCreated(currentState: UserState, userCreated: UserCreated): UserState =
    currentState.copy(
      name = userCreated.name
    )

  override def addedPerson(currentState: UserState, addedPerson: AddedPerson): UserState = {
    println("User.addedPerson -- before", "   ", addedPerson.userId, currentState.name, currentState.persons)

    val a = UserState(
      name = currentState.name,
      persons = Seq(addedPerson.personId)
    )
    println("User.addedPerson -- after", "   ", addedPerson.userId, a.name, a.persons)

    a
  }

  override def addPerson(currentState: UserState, personCreated: PersonCreated): EventSourcedEntity.Effect[Empty] = {
    effects
      .emitEvent(
        AddedPerson(
          personCreated.userId,
          personCreated.personId
        )
      )
      .thenReply(_ => Empty())
  }

  override def getContacts(currentState: UserState, listContacts: ListContacts): EventSourcedEntity.Effect[Contacts] =
    effects.reply(Contacts(currentState.invites.map(_.userId)))

  override def getContactInvitations(
      currentState: UserState,
      listContactsInvitations: ListContactsInvitations
  ): EventSourcedEntity.Effect[ContactInvites] =
    effects.reply(ContactInvites(currentState.invites.map(_.userId)))

  override def addContactInvitation(
      currentState: UserState,
      addContactInvitation: AddContactInvitation
  ): EventSourcedEntity.Effect[Empty] =
    effects.emitEvent(AddedContactInvitation(addContactInvitation.contact)).thenReply(_ => Empty())

  override def deleteContactInvitation(
      currentState: UserState,
      deleteContactInvitation: DeleteContactInvitation
  ): EventSourcedEntity.Effect[Empty] =
    effects.emitEvent(DeletedContactInvitation(deleteContactInvitation.contact)).thenReply(_ => Empty())

  override def addContact(currentState: UserState, addContact: AddContact): EventSourcedEntity.Effect[Empty] =
    effects.emitEvent(AddedContact(addContact.contact)).thenReply(_ => Empty())

  override def deleteContact(currentState: UserState, deleteContact: DeleteContact): EventSourcedEntity.Effect[Empty] =
    effects.emitEvent(DeletedContact(deleteContact.contact)).thenReply(_ => Empty())

  override def addedContact(currentState: UserState, addedContact: AddedContact): UserState =
    currentState.copy(
      contacts = currentState.contacts :+ Contact(addedContact.contact)
    )

  override def addedContactInvitation(
      currentState: UserState,
      addedContactInvitation: AddedContactInvitation
  ): UserState =
    currentState.copy(
      invites = currentState.invites :+ Contact(addedContactInvitation.contact)
    )

  override def deletedContact(currentState: UserState, deletedContact: DeletedContact): UserState =
    currentState.copy(
      contacts = currentState.contacts.filterNot(_.userId == deletedContact.contact)
    )

  override def deletedContactInvitation(
      currentState: UserState,
      deletedContactInvitation: DeletedContactInvitation
  ): UserState =
    currentState.copy(
      contacts = currentState.invites.filterNot(_.userId == deletedContactInvitation.contact)
    )

  override def addChatInvitation(currentState: UserState, cmd: AddChatInvitation): EventSourcedEntity.Effect[Empty] =
    effects
      .emitEvent(
        AddedChatInvitation(
          cmd.userId,
          cmd.chatId
        )
      )
      .thenReply(_ => Empty())

  override def deleteChatInvitation(
      currentState: UserState,
      cmd: DeleteContactInvitation
  ): EventSourcedEntity.Effect[Empty] =
    effects
      .emitEvent(
        DeletedContactInvitation(
          cmd.contact
        )
      )
      .thenReply(_ => Empty())

  override def acceptChatInvitation(
      currentState: UserState,
      cmd: AcceptChatInvitation
  ): EventSourcedEntity.Effect[Empty] =
    effects
      .emitEvent(
        AcceptedChatInvitation(
          cmd.chatId
        )
      )
      .thenReply(_ => Empty())

  override def refuseChatInvitation(
      currentState: UserState,
      cmd: RefuseChatInvitation
  ): EventSourcedEntity.Effect[Empty] =
    effects
      .emitEvent(
        RefusedChatInvitation(
          cmd.chatId
        )
      )
      .thenReply(_ => Empty())

  override def addedChatInvitation(currentState: UserState, addedChatInvitation: AddedChatInvitation): UserState =
    currentState.copy(
      chatInvitesIds = currentState.chatInvitesIds :+ addedChatInvitation.chatId
    )

  override def refusedChatInvitation(currentState: UserState, cmd: RefusedChatInvitation): UserState =
    currentState.copy(
      chatInvitesIds = (currentState.chatInvitesIds.toSet - cmd.chatId).toSeq
    )

  override def acceptedChatInvitation(
      currentState: UserState,
      cmd: AcceptedChatInvitation
  ): UserState =
    currentState.copy(
      chatInvitesIds = (currentState.chatInvitesIds.toSet - cmd.chatId).toSeq,
      acceptedChatsIds = currentState.acceptedChatsIds :+ cmd.chatId
    )

}
