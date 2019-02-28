package net.z3r05um.example.helloworld

import akka.NotUsed
import akka.stream.Materializer
import akka.stream.scaladsl.{Sink, Source}
import net.z3r05um.example.helloworld.grpc._

import scala.concurrent.Future

class GreeterServiceImpl(materializer: Materializer) extends GreeterService {
  import materializer.executionContext
  private implicit val mat: Materializer = materializer

  override def sayHello(in: HelloRequest): Future[HelloReply] = {
    println(s"sayJello to ${in.name}")
    Future.successful(HelloReply(s"Hello, ${in.name}"))
  }

  override def itKeepsTalking(in: Source[HelloRequest, NotUsed]): Future[HelloReply] = {
    println(s"sayHello to in stream...")
    in.runWith(Sink.seq)
      .map(elements => HelloReply(s"Hello, ${elements.map(_.name).mkString(", ")}"))
  }

  override def itKeepsReplying(in: HelloRequest): Source[HelloReply, NotUsed] = {
    println(s"sayHello to ${in.name} with stream of chars...")
    Source(s"Hello, ${in.name}".toList)
      .map(character => HelloReply(character.toString))
  }

  override def streamHellos(in: Source[HelloRequest, NotUsed]): Source[HelloReply, NotUsed] = {
    println(s"sayHello to stream...")
    in.map(request => HelloReply(s"Hello, ${request.name}"))
  }
}
