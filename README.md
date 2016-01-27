# sbt-javaversion

sbt-javaversioncheck is an sbt plugin to check current Java version.
This is a fork of Typesafe's [sbt-javaversioncheck](https://github.com/sbt/sbt-javaversioncheck).

The main differences are:
- This plugin expects a mandatory javaVersion key
- The javaVersion key is a String, not an Option\[String\]
- The plugin checks the java version before compilation, so that the compilation fails

## setup

This is an auto plugin, so you need sbt 0.13.5+. Put this in `project/javaversion.sbt`:

```scala
addSbtPlugin("com.agilogy" % "sbt-javaversion" % "0.1.0")
```

## usage

sbt-javaversion is a triggered plugin that is enabled automatically for all jvm projects (all projects that have `JvmPlugin`).

In your buid, you must define a javaVersion property to "1.8" or whatever:

```scala
lazy val fooProj = (project in file("foo")).
  settings(
    javaVersion := "1.8"
  )
```

Your build will fail during compilation if the Java version used to compile the project is not compatible with the declared Java version.

Currently, the jvm must match the exact prefix defined in javaVersion. We plan to check the version more properly (e.g.
allowing to compile a project with javaVersion := "1.7" with a 1.8 jvm).
