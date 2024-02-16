import xerial.sbt.Sonatype.GitHubHosting

ThisBuild / scalaVersion := "3.3.1"

ThisBuild / organization := "net.reactivecore"

// If there is a Tag starting with v, e.g. v0.3.0 use it as the build artefact version (e.g. 0.3.0)
val versionTag = sys.env
  .get("CI_COMMIT_TAG")
  .filter(_.startsWith("v"))
  .map(_.stripPrefix("v"))

val snapshotVersion = "0.2-SNAPSHOT"
val artefactVersion = versionTag.getOrElse(snapshotVersion)
ThisBuild / version := artefactVersion


def publishSettings = Seq(
  publishTo               := sonatypePublishToBundle.value,
  sonatypeBundleDirectory := (ThisBuild / baseDirectory).value / "target" / "sonatype-staging" / s"${version.value}",
  licenses                := Seq("APL2" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt")),
  homepage                := Some(url("https://github.com/reactivecore/quest")),
  sonatypeProjectHosting  := Some(GitHubHosting("reactivecore", "quest", "contact@reactivecore.de")),
  developers              := List(
    Developer(
      id = "nob13",
      name = "Norbert Schultz",
      email = "norbert.schultz@reactivecore.de",
      url = url("https://www.reactivecore.de")
    )
  ),
  publish / test          := {},
  publishLocal / test     := {}
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
