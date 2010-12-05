import sbt._
class Plugins(info: ProjectInfo) extends PluginDefinition(info) {
  val proguard = "org.scala-tools.sbt" % "sbt-proguard-plugin" % "0.0.4"
  val codaRepo = "Coda Hale's Repository" at "http://repo.codahale.com/"
  val assemblySBT = "com.codahale" % "assembly-sbt" % "0.1"
}
