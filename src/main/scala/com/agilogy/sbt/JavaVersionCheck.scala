package com.agilogy.sbt

import sbt._
import Keys._
import compiler.JavaTool
import plugins.JvmPlugin

import scala.util.Try

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
      JavaVersionCheck.check((javaVersion in javaVersionCheck).value, sys.props("java.specification.version"))
    },
    // we DO want to require the version check just for compile.
    compile in Compile <<= (compile in Compile).dependsOn(javaVersionCheck in Compile),
    compile in Test <<= (compile in Test).dependsOn(javaVersionCheck in Test)

  )
}

object JavaVersionCheck {

  def check(requiredJavaVersion: String, javaSpecificationVersion:String):String = {
    if(!isOk(requiredJavaVersion,javaSpecificationVersion)){
      sys.error(s"javac version $javaSpecificationVersion can't be used to compile, required java version is $requiredJavaVersion (due to javaVersion setting)")
    }else {
      javaSpecificationVersion
    }
  }

  def isOk(requiredJavaVersion: String, javaSpecificationVersion:String): Boolean = {
    val requiredParts = Try(requiredJavaVersion.split("""[\._]""").map(_.toInt)).getOrElse(sys.error(s"Illegal required java version $requiredJavaVersion"))
    val javaSpecificationParts = Try(javaSpecificationVersion.split("""[\._]""").map(_.toInt)).getOrElse(sys.error(s"Illegal actual java version $javaSpecificationVersion"))
    isOk(requiredParts, javaSpecificationParts)
  }

  def isOk(requiredJavaVersion:Array[Int], usedJavaVersion:Array[Int]):Boolean = {
    usedJavaVersion.zip(requiredJavaVersion).foldRight[Boolean](requiredJavaVersion.length <= usedJavaVersion.length){
      case ((used,req),acc) => (used > req) || (used == req && acc)
    }
  }
}

