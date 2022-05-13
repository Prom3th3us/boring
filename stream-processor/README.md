# stream-processor
In this repository we will use Kafka Streams to process a stream.

We have two topics:

- `chat-group-commands`
Will inform if a user has been added or removed from a chat
- `messages`
Will inform us of messages to a chat

We will expose one a new topic:
- `user-messages`
Will broadcast the message to each user of the chat 


So the idea is simple, we want event sourcing to keep the state of chats (the users inside a chat) and use that
state to broadcast a message to all the users of the chat.

We could diagram the topology as follows:

- We receive message 
- We retrieve the state of the chat (the users)
- We publish the message to all users
![simple_diagram](https://user-images.githubusercontent.com/9152392/168375063-b80659a6-461c-4665-926c-e8e85d650efc.png)

