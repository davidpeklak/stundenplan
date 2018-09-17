
scalaVersion := "2.12.6"

val Specs2 = "org.specs2" %% "specs2-core" % "4.3.4"
val Specs2Mock = "org.specs2" %% "specs2-mock" % "4.3.4"

val json4s = "org.json4s" %% "json4s-native" % "3.6.1"

val akkaHttp = "com.typesafe.akka" %% "akka-http"   % "10.1.5"
val akkaStream = "com.typesafe.akka" %% "akka-stream" % "2.5.12"

/**
  * Defines the data entities, without any logic
  */
lazy val stundenplanEntities = (project in file("entities"))
  .settings(
    name := "stundenplan-entities",
    organization := "davidpeklak"
  )

/**
  * Defines validations of the data entities
  */
lazy val stundenplanValidation = (project in file("validation"))
  .settings(
    name := "stundenplan-validation",
    organization := "davidpeklak",
    libraryDependencies += Specs2 % "test"
  ).dependsOn(stundenplanEntities)

/**
  * Defines the business-logic
  */
lazy val stundenplanLogic = (project in file("logic"))
  .settings(
    name := "stundenplan-logic",
    organization := "davidpeklak",
    libraryDependencies ++= Seq(
      Specs2 % "test",
      Specs2Mock % "test"
    )
  ).dependsOn(stundenplanEntities)

/**
  * Implements Storage as FileStorage
  */
lazy val stundenplanFileStorage = (project in file("filestorage"))
  .settings(
    name := "stundenplan-filestorage",
    organization := "davidpeklak",
    libraryDependencies += Specs2 % "test"
  ).dependsOn(stundenplanLogic)

/**
  * A frontend based on the play framework
  */
lazy val stundenplanPlayFrontend = (project in file("playfrontend"))
  .enablePlugins(PlayScala)
  .settings(
    name := "stundenplan-playfrontend",
    organization := "davidpeklak",
    libraryDependencies ++= Seq(guice, json4s)
  ).dependsOn(stundenplanLogic, stundenplanFileStorage)

/**
  * An http interface based on akka-http
  */
lazy val stundenplanAkkaHttp = (project in file("akkahttp"))
  .settings(
    name := "stundenplan-akka-http",
    organization := "davidpeklak",
    libraryDependencies ++= Seq(json4s, akkaHttp, akkaStream)
  ).dependsOn(stundenplanLogic, stundenplanFileStorage)
