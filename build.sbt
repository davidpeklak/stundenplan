scalaVersion := "2.12.6"

val Specs2 = "org.specs2" %% "specs2-core" % "4.3.4"

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
    libraryDependencies += Specs2 % "test"
  ).dependsOn(stundenplanEntities)
