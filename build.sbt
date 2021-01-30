val originalJvmOptions = sys.process.javaVmArguments.filter(
  a => Seq("-Xmx", "-Xms", "-XX").exists(a.startsWith)
)

val unfiltered = Seq(
  "filter", "filter-async", "agents", "uploads", "util", "jetty", "netty-server",
  "netty", "json4s", "netty-websockets", "scalatest", "directives"
).map{n => "ws.unfiltered" %% ("unfiltered-" + n ) % "0.10.1"}

val scalaz = Seq(
  "core", "effect", "iteratee", "scalacheck-binding"
).map{ m =>
  "org.scalaz" %% ("scalaz-" + m) % "7.3.3"
}

enablePlugins(PlayScala)

scalaVersion := "2.13.4"

scalacOptions ++= Seq("-Xlint", "-deprecation", "-language:_", "-unchecked")

cleanFiles ++= Seq(file("logs"))

javaOptions ++= originalJvmOptions

licenses := Seq("MIT" -> url("http://opensource.org/licenses/MIT"))

libraryDependencies += guice
libraryDependencies += specs2

libraryDependencies ++= Seq(
  "com.chuusai" %% "shapeless" % "2.3.3",
  "org.scalikejdbc" %% "scalikejdbc" % "3.5.0",
) ++ scalaz ++ unfiltered

libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % "test"
libraryDependencies += ws
