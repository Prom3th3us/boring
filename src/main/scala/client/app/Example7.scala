package client.app

import client.app.Example7.DataStructureUsingDependentDatatypes.a

object Example7 extends App {

  import DataStructureUsingDependentDatatypes._
  println("Printing a")
  println(a.value)
  println("Printing n")
  println(a.n.map(_.value))

  extension [A](thiz: A) def toTuple2[B](that: B): (A, B) = thiz -> that

  object DataStructureUsingDependentDatatypes:
    trait Chain[A] {
      type is = A
      type B
      type next = Chain[B]
      val value: A
      val n: Option[next] = None
    }

    // TODO use inline as much as possible! Anything you can!
    val a = new Chain[String] {
      override val value: String = "here"
      override type B = Int
      override val n = Some(
        new Chain[Int] {
          override val value: Int = 1
        }
      )
    }

}
