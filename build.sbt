scalaVersion := "2.12.6"

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
    organization := "davidpeklak"
  ).dependsOn(stundenplanEntities)
