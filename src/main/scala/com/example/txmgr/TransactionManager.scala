package com.example.txmgr

import java.time.LocalDateTime

import com.example.txmgr.Model._

import scala.concurrent.stm._

/**
  * In-memory transaction manager based on ScalaSTM.
  */
class TransactionManager {

  // ScalaSTM data structures for storing accounts and transaction log
  private val map = TMap.empty[String, Int]
  private val log = TSet.empty[Transfer]

  // ordering for java.time.LocalDateTime, used in sorting
  implicit object DateOrdering extends Ordering[LocalDateTime] {
    def compare(x: LocalDateTime, y: LocalDateTime) = x compareTo y
  }

  /**
    * Transfer money between accounts.
    *
    * @param transfer info about the transaction
    */
  def transfer(transfer: Transfer) {
    atomic { implicit txn =>
      log.add(transfer.copy(time = Some(LocalDateTime.now())))
      map.put(transfer.from, map(transfer.from) - transfer.amount)
      map.put(transfer.to, map(transfer.to) + transfer.amount)
    }
  }

  /**
    * Get the amount of money on the account.
    *
    * @param account account name
    * @return amount of money on the account
    */
  def getAmount(account: String) = map.single(account)

  /**
    * Add account to map.
    *
    * @param account user account
    */
  def addAccount(account: Account) {
    atomic { implicit txn =>
      map.put(account.name, account.amount)
    }
  }

  /**
    * Retreive the log of operations for specified account.
    *
    * @param account account name
    * @return log of operations with the specified account as a sender or a receiver
    */
  def getLog(account: String): List[Transfer] = {
    atomic { implicit txn =>
      log.filter(rec => rec.from == account || rec.to == account)
        .toList.sortBy(_.time)
    }
  }

}

