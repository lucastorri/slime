import ReleaseTransformations._

lazy val commonSettings = Seq(
  version := "0.2.0",
  scalaVersion := "2.12.0",
  scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature"),
  crossScalaVersions := Seq("2.11.8", "2.12.0"),
  organization := "se.petterarvidsson",
  licenses := Seq("MIT License" -> url("https://opensource.org/licenses/MIT")),
  libraryDependencies ++= Seq(
    "org.slf4j" % "slf4j-api" % "1.7.25",
    "ch.qos.logback" % "logback-core" % "1.2.2",
    "ch.qos.logback" % "logback-classic" % "1.2.2",
    "ch.qos.logback" % "logback-access" % "1.2.2",
    "net.logstash.logback" % "logstash-logback-encoder" % "4.11",
    "com.chuusai" %% "shapeless" % "2.3.2"
  ),
  publishMavenStyle := true
)

releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  setNextVersion,
  commitNextVersion,
  pushChanges
)
