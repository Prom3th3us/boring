package com.example.domain

import com.akkaserverless.scalasdk.testkit.ValueEntityResult
import com.akkaserverless.scalasdk.valueentity.ValueEntity
import com.example
import com.google.protobuf.empty.Empty
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class UserSpec
    extends AnyWordSpec
    with Matchers {

  "User" must {

    "have example test that can be removed" in {
      val testKit = UserTestKit(new User(_))
      // use the testkit to execute a command
      // and verify final updated state:
      // val result = testKit.someOperation(SomeRequest)
      // verify the response
      // val actualResponse = result.getReply()
      // actualResponse shouldBe expectedResponse
      // verify the final state after the command
      // testKit.currentState() shouldBe expectedState
    }

    "handle command register" in {
      val testKit = UserTestKit(new User(_))
      // val result = testKit.register(example.RegisterUserValue(...))
    }

    "handle command find" in {
      val testKit = UserTestKit(new User(_))
      // val result = testKit.find(example.FindUserValue(...))
    }

    "handle command addPerson" in {
      val testKit = UserTestKit(new User(_))
      // val result = testKit.addPerson(example.AddPersonValue(...))
    }

  }
}
