scalaVersion := "2.12.6"

lazy val stundenplanEntities = (project in file("."))
  .settings(
    name := "stundenplan-entities",
    organization := "davidpeklak"
  )
