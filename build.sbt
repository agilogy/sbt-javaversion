import bintray.Keys._

sbtPlugin := true

name := "sbt-javaversioncheck"

organization := "com.agilogy"

version := "0.1.0"

description := "sbt plugin to check Java version"

licenses := Seq("MIT License" -> url("http://opensource.org/licenses/MIT"))

scalacOptions := Seq("-deprecation", "-unchecked")

// --> bintray

Seq(bintrayPublishSettings:_*)

repository in bintray := "scala"

bintrayOrganization in bintray := Some("agilogy")

packageLabels in bintray := Seq("scala")

licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0.html"))

// <-- bintray

publishMavenStyle := isSnapshot.value

publishTo := {
  val nexus = "http://188.166.95.201:8081/content/repositories/snapshots"
  if (isSnapshot.value) Some("snapshots"  at nexus)
  else publishTo.value
}