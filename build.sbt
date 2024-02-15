ThisBuild / scalaVersion := "3.3.2"

ThisBuild / organization := "net.reactivecore"

// If there is a Tag starting with v, e.g. v0.3.0 use it as the build artefact version (e.g. 0.3.0)
val versionTag = sys.env
  .get("CI_COMMIT_TAG")
  .filter(_.startsWith("v"))
  .map(_.stripPrefix("v"))

val snapshotVersion = "0.1-SNAPSHOT"
val artefactVersion = versionTag.getOrElse(snapshotVersion)
ThisBuild / version := artefactVersion


val publishSettings = Seq(
  publishTo           := {
    val nexus = "https://sonatype.rcxt.de/repository/reactivecore/"
    if (isSnapshot.value)
      Some("snapshots" at nexus)
    else
      Some("releases" at nexus)
  },
  publishMavenStyle   := true,
  credentials += {
    for {
      username <- sys.env.get("SONATYPE_USERNAME")
      password <- sys.env.get("SONATYPE_PASSWORD")
    } yield {
      Credentials("Sonatype Nexus Repository Manager", "sonatype.rcxt.de", username, password)
    }
  }.getOrElse(
    Credentials(Path.userHome / ".sbt" / "sonatype.rcxt.de.credentials")
  ),
  publish / test      := {},
  publishLocal / test := {}
)

usePgpKeyHex("77D0E9E04837F8CBBCD56429897A43978251C225")


val ScalaTestVersion = "3.2.17"

lazy val testSettings = libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest"          % ScalaTestVersion % "test",
  "org.scalatest" %% "scalatest-flatspec" % ScalaTestVersion % "test"
)


lazy val root = (project in file("."))
  .settings(
    name := "quest",
    testSettings,
    publishSettings
  )
