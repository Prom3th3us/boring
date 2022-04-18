package client.app

import client.app.Example8.MacroExample.*

@main def test(): Unit =
  val show = mcr
  println(show.show(10))
