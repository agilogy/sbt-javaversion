sbtPlugin := true

organization := "com.agilogy"

name := "sbt-javaversion"

version := "0.2.1"

description := "sbt plugin to check Java version"

licenses := Seq("MIT License" -> url("http://opensource.org/licenses/MIT"))

scalacOptions := Seq("-deprecation", "-unchecked")

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.1.3" % "test"
)
// --> bintray

publishMavenStyle := false

bintrayRepository := "sbt-plugins"

bintrayOrganization := Some("agilogy")

licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0.html"))

// <-- bintray
