# Prerequisites
- Scala 2.11
- sbt 0.13.11

# Building
`sbt assembly`

# Testing
`sbt test`

# Running
`java -jar target/scala-2.11/txmgr-assembly-1.0.jar`
Server opens at http://localhost:8080

# API
## Getting amount of money on the account

GET /account/{name}
Content-Type: application/json

Returns the amount of money on the account:
`{"name": "a", "amount": 100}`

## Creating an account
```
POST /account
Content-Type: application/json

{"name": "a", "amount": 100}
```

Creates account with name "a" and initial amount 100 units.

## Transferring money between accounts

```
POST /transfer
Content-Type: application/json

{"from": "a", "to": "b", "amount": 10}
```

Transfers 10 units from account "a" to account "b".

## Retrieving the log of transactions for specific account
`GET /transfer/account/{name}`

Returns the log of transactions in which this account participated.

[{"time": "2016-06-28T09:55:22.426", "from": "a", "to": "b", "amount": 5}]
