package com.example.txmgr

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.ExceptionHandler
import codes.reactive.scalatime._
import com.example.txmgr.Model._
import spray.json.{DefaultJsonProtocol, JsString, JsValue, RootJsonFormat}

/**
  * RESTful API for TransactionManager.
  */
object Server extends SprayJsonSupport with DefaultJsonProtocol {


  // implicit for serializing LocalDateTime to JSON
  implicit object DateJsonFormat extends RootJsonFormat[LocalDateTime] {

    private val parserISO: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    override def write(obj: LocalDateTime) = JsString(parserISO.format(obj))

    override def read(json: JsValue): LocalDateTime = json match {
      case JsString(s) => parserISO.parse(s, LocalDateTime.from _)
      case _ => throw new RuntimeException() // exception type is irrelevant here, so using generic RuntimeException
    }
  }


  // exception handler for all unhandled issues
  val exceptionHandler = ExceptionHandler {
    case _: NoSuchElementException =>
      complete(HttpResponse(BadRequest, entity = "The specified account does not exist."))
  }


  // jsonFormats for domain objects
  implicit val transferFormat = jsonFormat4(Transfer)
  implicit val accountFormat = jsonFormat2(Account)


  // transaction manager
  val txmgr = new TransactionManager()

  val route =
    handleExceptions(exceptionHandler) {
      pathPrefix("transfer") {
        post {
          entity(as[Transfer]) { transfer =>
            txmgr.transfer(transfer)
            complete(OK)
          }
        } ~ path("account" / Segment) { account =>
          get {
            complete(txmgr.getLog(account))
          }
        }
      } ~ pathPrefix("account") {
        post {
          entity(as[Account]) { account =>
            txmgr.addAccount(account)
            complete(OK)
          }
        } ~ path(Segment) { account =>
          get {
            complete(Account(account, txmgr.getAmount(account)))
          }
        }
      }
    }


}
