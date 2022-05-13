import application._
import domain.chat_group.commands.ChatGroupCommands
import domain.chat_group.commands.ChatGroupCommands.AddUser
import domain.chat_group.entities.entities.{ ChatGroupId, Message, UserId }
import domain.chat_group.serialization.json._
import domain.chat_group.state.ChatGroupState
import org.apache.kafka.common.serialization.{ Serde, Serdes }
import org.apache.kafka.streams.StreamsConfig
import org.apache.kafka.streams.scala.StreamsBuilder
import org.scalatest.BeforeAndAfterAll
import org.scalatest.concurrent.Eventually
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import utils.JsonSerializer._

import java.util.Properties
import scala.concurrent.duration._
import scala.language.implicitConversions

class ApplicationSpec extends AnyWordSpec with Matchers with Eventually with BeforeAndAfterAll {
  val (inTopic, outTopic) = ("in", "out")

  val stringSerde: Serde[String] = Serdes.String()

  "Application" should {

    "aggregate the users into Chats, and broadcast the messages to the users" in {

      implicit val builder = new StreamsBuilder
      ChatGroupAggregate()
      BroadcastMessage()
      val topology = builder.build()
      import org.apache.kafka.streams.TopologyTestDriver

      implicit val kafkaProps: Properties = {
        val props = new Properties
        props.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, "exactly_once")
        props.put(
          StreamsConfig.DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG,
          "org.apache.kafka.streams.errors.LogAndContinueExceptionHandler"
        )
        props.put(
          StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG,
          org.apache.kafka.streams.scala.serialization.Serdes.stringSerde.getClass
        )
        props.put(
          StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG,
          org.apache.kafka.streams.scala.serialization.Serdes.stringSerde.getClass
        )
        props
      }
      val testDriver = new TopologyTestDriver(topology, kafkaProps)
      println(topology.describe())

      val `chat-group-commands` = testDriver.createInputTopic(
        "chat-group-commands",
        JsonSerializer[ChatGroupId]()(chatGroupIdFormat),
        JsonSerializer[ChatGroupCommands]()(chatGroupCommandsFormat)
      )

      val `messages` = testDriver.createInputTopic(
        "messages",
        JsonSerializer[ChatGroupId]()(chatGroupIdFormat),
        JsonSerializer[Message]()(messageFormat)
      )

      val `chat-group-snapshots` = testDriver.createOutputTopic(
        "chat-group-snapshots",
        JsonSerializer[ChatGroupId]()(chatGroupIdFormat),
        JsonSerializer[ChatGroupState]()(chatGroupStateFormat)
      )

      val `user-messages` = testDriver.createOutputTopic(
        "user-messages",
        JsonSerializer[UserId]()(userIdFormat),
        JsonSerializer[Message]()(messageFormat)
      )

      val setup: ChatGroupId => UserId => Unit = { chatGroupId => userId =>
        Seq(AddUser(userId))
          .foreach(`chat-group-commands`.pipeInput(chatGroupId, _))
      }

      setup(ChatGroupId("chat-A"))(UserId("user-A"))
      setup(ChatGroupId("chat-A"))(UserId("user-B"))
      `messages`.pipeInput(ChatGroupId("chat-A"), Message("hello"))

      implicit val patienceConfig: PatienceConfig =
        PatienceConfig(150.seconds, 15.seconds)

      var chatGroup_snapshot: Map[ChatGroupId, ChatGroupState] = Map.empty
      eventually {
        import scala.jdk.CollectionConverters._
        chatGroup_snapshot = chatGroup_snapshot ++ `chat-group-snapshots`.readKeyValuesToMap().asScala
        println(chatGroup_snapshot)
        chatGroup_snapshot should be(
          Map(
            ChatGroupId("chat-A") -> ChatGroupState(
              users = Set(
                UserId("user-A"),
                UserId("user-B")
              )
            )
          )
        )
      }

      var acc: Seq[(UserId, Message)] = Seq.empty
      eventually {
        import scala.jdk.CollectionConverters._
        acc = acc ++ `user-messages`.readKeyValuesToList().asScala.map(e => e.key -> e.value)
        println(acc)
        acc should be(
          Seq(
            UserId("user-A") -> Message("hello"),
            UserId("user-B") -> Message("hello")
          )
        )
      }
      // END OF TEST
    }
  }
}
