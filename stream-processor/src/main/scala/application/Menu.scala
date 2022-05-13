package application

import scopt.OParser

object Menu {

  val builder = OParser.builder[Options]

  import scopt.OParser

  def apply(args: Array[String]) =
    OParser.parse(menu, args, Options())

  private def menu = {
    import builder._
    OParser.sequence(
      programName("scopt"),
      head("scopt", "4.x"),
      // option -f, --foo
      opt[Boolean]("chatGroupAggregate")
        .action((x, c) => c.copy(chatGroupAggregate = true))
        .text("Start the core stream processing"),
      opt[Boolean]("broadcastMessages")
        .action((x, c) => c.copy(broadcastMessages = true))
        .text("Start the broadcasting of messages"),
      help("help").text("""
        |Use
        | --broadcastMessages to start the broadcasting of messages
        | --chatGroupAggregate to start the core stream system
        |""".stripMargin)
    )
  }

}
