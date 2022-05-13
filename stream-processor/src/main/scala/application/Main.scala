package application

object Main {

  def main(args: Array[String]): Unit = {
    Menu.apply(args) match {
      case Some(options) =>
        infrastructure.kafka.Setup.apply { implicit builder =>
          if (options.chatGroupAggregate)
            ChatGroupAggregate()
          if (options.broadcastMessages)
            BroadcastMessage()
        }

      case _ =>
        println("""
                  |To see the available options use --help 
                  |""".stripMargin)
    }

  }
}
