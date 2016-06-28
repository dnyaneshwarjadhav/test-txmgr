name := "txmgr"

version := "1.0"

scalaVersion := "2.11.8"

scalacOptions += "-target:jvm-1.8"

javacOptions in Compile ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint:unchecked", "-Xlint:deprecation")

libraryDependencies ++= Seq(
  "org.scala-stm" %% "scala-stm" % "0.7",
  "com.typesafe.akka" %% "akka-http-core" % "2.4.7",
  "com.typesafe.akka" %% "akka-http-experimental" % "2.4.7",
  "com.typesafe.akka" %% "akka-http-spray-json-experimental" % "2.4.7",
  "codes.reactive" %% "scala-time" % "0.4.0",

  "com.typesafe.akka" %% "akka-http-testkit" % "2.4.7" % "test",
  "org.scalactic" %% "scalactic" % "2.2.6" % "test",
  "org.scalatest" %% "scalatest" % "2.2.6" % "test"
)

lazy val app = (project in file("app")).
  settings(
    mainClass in assembly := Some("com.example.txmgr.Main")
  )