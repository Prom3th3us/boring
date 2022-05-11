# Akka Serverless Bug Report
## Bug 2

This bug is about messaging using Kafka.

If you publish a message to a topic, and you use for the message 
definition a .proto definition of your own, then the receiver 
throws this error upon receiving the message.

```yaml
com.akkaserverless.proxy.UserFunctionErrors$ReportableError: 
  No method can be found on service [actor.user.UserService] to handle protobuf type of
    [type.googleapis.com/actor.messaging.domain.SendMessageDtoM] on input 
    [topic: \"message\"].
```

The main consequence this has over the codebase is *coupling*:
**Now because the producer must reference the .proto definition of the consumer
a producer cannot serve messages to a topic that is going to be consumed
by multiple different consumers.**


It seems like you must publish to Kafka using the .proto
definition defined by the receiver of the message.

## Steps to reproduce

sbt run

docker-compose up

curl POST   localhost:9000/message {
"user_id": "lola2",
"message": "hello, lola2 again!"
}

curl GET   localhost:9000/user/lola2 

