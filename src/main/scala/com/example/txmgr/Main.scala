package com.example.txmgr

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer

import scala.io.StdIn

/**
  * Main runnable class for initializing the actor system and web server.
  */
object Main extends App {

  implicit val system = ActorSystem("txmgr-system")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  val bindingFuture = Http().bindAndHandle(Server.route, "localhost", 8080)
  println(s"Server started at http://localhost:8080, press Enter to stop.")
  StdIn.readLine()
  bindingFuture.flatMap(_.unbind()).onComplete(_ => system.terminate())

}
