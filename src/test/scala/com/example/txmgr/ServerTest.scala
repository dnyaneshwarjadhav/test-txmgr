package com.example.txmgr

import java.util.concurrent.{ExecutorService, Executors, TimeUnit}

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.example.txmgr.Model.{Account, Transfer}
import com.example.txmgr.Server._
import org.scalatest.{Matchers, WordSpec}
import spray.json.DefaultJsonProtocol

import scala.util.Random

class ServerTest extends WordSpec
  with Matchers with ScalatestRouteTest with SprayJsonSupport with DefaultJsonProtocol {

  val accounts = Seq("a", "b", "c", "d", "e")

  "Transaction manager" should {

    "create new accounts" in {
      accounts.foreach(account => {
        Post("/account", Account(account, 100)) ~> route ~> check {
          status shouldBe StatusCodes.OK
          txmgr.getAmount(account) shouldBe 100
        }
      })
    }

    "receive the amount of money currently on the account a" in {
      Get("/account/a") ~> route ~> check {
        status shouldBe StatusCodes.OK
        entityAs[Account] shouldBe Account("a", 100)
      }
    }

    "transfer money from account a to account b" in {
      Post("/transfer", Transfer(None, "a", "b", 50)) ~> route ~> check {
        status shouldBe StatusCodes.OK
        txmgr.getAmount("a") shouldBe 50
        txmgr.getAmount("b") shouldBe 150
      }
    }

    "retrieve the log of operations for a specific account" in {
      Get("/transfer/account/a") ~> route ~> check {
        entityAs[List[Transfer]] should matchPattern { case List(Transfer(Some(_), "a", "b", 50)) => }
      }
    }

    "return status code 400 for getting an inexisting account" in {
      Get("/account/f") ~> route ~> check {
        status shouldBe StatusCodes.BadRequest
      }
    }

    "return status code 400 for transferring to/from an inexisting account" in {
      Post("/transfer", Transfer(None, "a", "f", 50)) ~> route ~> check {
        status shouldBe StatusCodes.BadRequest
      }
    }

    "hold the total sum invariant under stress" in {

      val pool: ExecutorService = Executors.newFixedThreadPool(20)

      val rng = Random

      1 to 100000 foreach { _ =>
        pool.submit(new Runnable() {
          def run() {
            val account1 = accounts(rng.nextInt(5))
            val account2 = accounts(rng.nextInt(5))
            val amount = rng.nextInt(10000)
            Post("/transfer", Transfer(None, account1, account2, amount)) ~> route ~> check {
              status shouldBe StatusCodes.OK
            }
          }
        })
      }

      pool.shutdown()
      pool.awaitTermination(10, TimeUnit.MINUTES)

      accounts.map(txmgr.getAmount).sum shouldBe 500
    }

  }

}
