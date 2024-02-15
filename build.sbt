ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.2"

val ScalaTestVersion = "3.2.17"

lazy val testSettings = libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest"          % ScalaTestVersion % "test",
  "org.scalatest" %% "scalatest-flatspec" % ScalaTestVersion % "test"
)


lazy val root = (project in file("."))
  .settings(
    name := "quest",
    testSettings
  )
