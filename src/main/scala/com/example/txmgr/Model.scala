package com.example.txmgr

import java.time.LocalDateTime

/**
  * Model case classes.
  */
object Model {

  /**
    * Model of a transfer between accounts.
    *
    * @param time   local server time at which the transfer happened
    * @param from   source account
    * @param to     destination account
    * @param amount amount of money transferred
    */
  case class Transfer(time: Option[LocalDateTime], from: String, to: String, amount: Int)

  /**
    * Model of a user account.
    *
    * @param name   account name
    * @param amount amount of money on the account
    */
  case class Account(name: String, amount: Int)

}
