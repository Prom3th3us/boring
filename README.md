# Akka Serverless Bug Report
## Bug 1

This bug is about protobuf.

If you dont split your protobuf definitions using
one file for the service and one for the definition of the 
data transfer objects, then even though the project compiles
it will throw an unexpected null pointer exception.


```yaml
[error] Exception in thread "main" java.lang.NullPointerException
  [error]         at com.akkaserverless.javasdk.impl.AnySupport.resolveServiceDescriptor(AnySupport.scala:337)
  [error]         at com.akkaserverless.javasdk.AkkaServerless$LowLevelRegistration.registerEventSourcedEntity(AkkaServerless.java:88)
  [error]         at com.akkaserverless.javasdk.AkkaServerless.register(AkkaServerless.java:318)
  [error]         at com.akkaserverless.scalasdk.AkkaServerless.register(AkkaServerless.scala:134)
  [error]         at actor.AkkaServerlessFactory$.withComponents(AkkaServerlessFactory.scala:31)
  [error]         at actor.Main$.createAkkaServerless(Main.scala:24)
  [error]         at actor.Main$.main(Main.scala:29)
  [error]         at actor.Main.main(Main.scala)

```

It seems like you must split your definitions of 
services and data transfer objects between multiple 
protobuf files.

## Steps to reproduce

sbt run
