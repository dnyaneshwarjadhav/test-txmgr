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

```
GET /account/{name}
Content-Type: application/json
```

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

`[{"time": "2016-06-28T09:55:22.426", "from": "a", "to": "b", "amount": 5}]`

# Test assignment

## Transaction Manager

Design and implement a RESTful API (including data model and the backing implementation) for money transfers between internal users/accounts.

Explicit requirements:

1. keep it simple and to the point (e.g. no need to implement any authentication, assume the APi is invoked by another internal system/service)

2. use whatever frameworks/libraries you like (except Spring, sorry!) but don't forget about the requirement #1

3. the data store should run in­-memory for the sake of this test

4. the final result should be executable as a standalone program (should not require a pre­installed container/server)

5. demonstrate with tests that the API works as expected

Implicit requirements:

1. the code produced by you is expected to be of good quality.

2. there are no detailed requirements, use common sense.

Please put your work on github or bitbucket.
