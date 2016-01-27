package com.agilogy.sbt

import sbt._
import Keys._
import compiler.JavaTool
import plugins.JvmPlugin

object JavaVersionCheckPlugin extends sbt.AutoPlugin {
//  val defaultJavaVersion: String = "1.8"

  object autoImport {
    lazy val javaVersion = settingKey[String]("java version prefix required by javaVersionCheck")
    lazy val javaVersionCheck = taskKey[String]("checks the Java version vs. javaVersion, returns actual version")
  }

  import autoImport._
  override def trigger: PluginTrigger = allRequirements
  override def requires: JvmPlugin.type = JvmPlugin
  override lazy val projectSettings = javaVersionCheckSettings

//  private val javaVersionNotDefinedErrorMsg = """javaVersion is not defined! Add javaVersion := "1.8" (or whatever) to your build"""
//  private lazy val failJavaVersionDefinition = taskKey[String](sys.error(javaVersionNotDefinedErrorMsg))

  def javaVersionCheckSettings: Seq[Setting[_]] = Seq(

//    javaVersion in javaVersionCheck := (javaVersion in javaVersionCheck).or(javaVersion)
    javaVersionCheck := {
      val log = streams.value.log
      val javac = (compileInputs in (Compile, compile)).value.compilers.javac
      JavaVersionCheck((javaVersion in javaVersionCheck)/*.or(failJavaVersionDefinition)*/.value, javac, log)
    },
    // we DO want to require the version check just for compile.
    compile in Compile <<= (compile in Compile).dependsOn(javaVersionCheck in Compile),
    compile in Test <<= (compile in Test).dependsOn(javaVersionCheck in Test)

  )
}

object JavaVersionCheck {
  def apply(declaredJavaVersion: String, javac: JavaTool, realLog: Logger): String = {
    val captureVersionLog = new Logger() {
      var captured: Option[String] = None
       def log(level: Level.Value, message: => String): Unit = {
         val m = message
         if (level == Level.Warn && m.startsWith("javac ")) {
           captured = Some(m.substring("javac ".length).trim)
         } else {
           realLog.log(level, m)
         }
       }
      def success(message: => String): Unit = realLog.success(message)
      def trace(t: => Throwable): Unit = realLog.trace(t)
    }
    javac(sources = Nil,
      classpath = Nil,
      outputDirectory = file("."),
      options = Seq("-version"))(captureVersionLog)
    val actualJavaVersion: String = captureVersionLog.captured getOrElse {sys.error("failed to get or parse the output of javac -version")}
    if (!actualJavaVersion.startsWith(declaredJavaVersion)) {
      sys.error(s"javac version $actualJavaVersion can't be used to compile, java version is expected to start with $declaredJavaVersion (due to javaVersion setting)")
    }
    actualJavaVersion
  }
}

