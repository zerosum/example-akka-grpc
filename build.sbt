ThisBuild / name := "example-akka-grpc"
ThisBuild / version := "0.1"
ThisBuild / scalaVersion := "2.12.8"

lazy val proto = (project in file("proto"))
  .enablePlugins(AkkaGrpcPlugin)

lazy val server = (project in file("server"))
  .dependsOn(proto)
  .enablePlugins(JavaAgent, AkkaGrpcPlugin)
  .settings(
    javaAgents += "org.mortbay.jetty.alpn" % "jetty-alpn-agent" % "2.0.9" % "runtime;test"
  )

lazy val client = (project in file("client"))
  .dependsOn(proto)
  .enablePlugins(AkkaGrpcPlugin)