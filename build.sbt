sbtPlugin := true

organization := "com.agilogy"

name := "sbt-javaversion"

version := "0.1.0"

description := "sbt plugin to check Java version"

licenses := Seq("MIT License" -> url("http://opensource.org/licenses/MIT"))

scalacOptions := Seq("-deprecation", "-unchecked")

// --> bintray

bintrayRepository := "scala"

bintrayOrganization := Some("agilogy")

licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0.html"))

// <-- bintray
