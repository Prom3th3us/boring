package actor

import actor.chat.Chat
import actor.person.Person
import actor.user.User
import actor.messaging.MessagingServiceAction
import com.akkaserverless.scalasdk.AkkaServerless
import org.slf4j.LoggerFactory

// This class was initially generated based on the .proto definition by Akka Serverless tooling.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

object Main {

  private val log = LoggerFactory.getLogger("actor.Main")

  def createAkkaServerless(): AkkaServerless = {
    // The AkkaServerlessFactory automatically registers any generated Actions, Views or Entities,
    // and is kept up-to-date with any changes in your protobuf definitions.
    // If you prefer, you may remove this and manually register these components in a
    // `AkkaServerless()` instance.
    AkkaServerlessFactory.withComponents(new Chat(_), new Person(_), new User(_), new MessagingServiceAction(_))
  }

  def main(args: Array[String]): Unit = {
    log.info("starting the Akka Serverless service")
    createAkkaServerless().start()
  }
}