import sbt._
import sbt.FileUtilities._
import java.io.File

class JunctionBench(info: ProjectInfo) extends DefaultProject(info) with ProguardProject with assembly.AssemblyBuilder{

  import Configurations.{Compile, CompilerPlugin, Default, Provided, Runtime, Test}

  val scalaToolsSnapshots = "Scala-Tools Maven2 Snapshots Repository" at "http://scala-tools.org/repo-snapshots"
  val stanfordRepo = "Stanford Maven 2 Repo" at "http://prpl.stanford.edu:8081/nexus/content/groups/public"
  val junction = "edu.stanford.prpl.junction" % "JAVAJunction" % "0.6.7-SNAPSHOT"


  //project name
  override val artifactID = "JunctionBench"

  //program entry point
  override def mainClass: Option[String] = Some("Main")

  //proguard
  override def proguardOptions = List(
    "-keepclasseswithmembers public class * { public static void main(java.lang.String[]); }",
    "-dontoptimize",
    "-dontobfuscate",
    proguardKeepLimitedSerializability,
    proguardKeepAllScala,
    "-keep interface scala.ScalaObject"
  )

  override def proguardInJars = Path.fromFile(scalaLibraryJar) +++ super.proguardInJars

}

