# Akka Serverless
## Kafka Messaging

This branch is about messaging using Kafka.

If you publish a message to a topic called message
the entity User will receive the message and update its state

## Steps to reproduce

sbt run

docker-compose up

curl POST   localhost:9000/message {
"user_id": "pepe",
"message": "hello, pepe again!"
}

curl GET   localhost:9000/user/pepe 

> {
"name": "",
"persons": [],
"messages": [hello, pepe again!]
}