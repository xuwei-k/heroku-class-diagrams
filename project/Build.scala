import sbt._,Keys._
import com.heroku.sbt.HerokuPlugin

object build extends Build {

  val originalJvmOptions = sys.process.javaVmArguments.filter(
    a => Seq("-Xmx", "-Xms", "-XX").exists(a.startsWith)
  )

  val unfiltered = Seq(
    "filter","filter-async","agents","uploads","util","jetty","jetty-ajp","netty-server",
    "netty","json4s","netty-websockets","oauth","scalatest","directives"
  ).map{n => "net.databinder" %% ("unfiltered-" + n ) % "0.8.1"}

  val scalaz = Seq(
    "core","concurrent","effect","iteratee","scalacheck-binding","typelevel"
  ).map{ m =>
    "org.scalaz" %% ("scalaz-" + m) % "7.1.0"
  }

  val spire = Seq("spire", "spire-scalacheck-binding").map("org.spire-math" %% _ % "0.8.2")

  val main = Project(
    "heroku-class-diagrams", file(".")
  ).enablePlugins(play.PlayScala).enablePlugins(HerokuPlugin).settings(
    scalaVersion := "2.10.4",
    HerokuPlugin.autoImport.herokuAppName in Compile := "class-diagrams",
    scalacOptions ++= Seq("-Xlint", "-deprecation", "-language:_", "-unchecked"),
    cleanFiles ++= Seq(file("logs")),
    javaOptions ++= originalJvmOptions,
    licenses := Seq("MIT" -> url("http://opensource.org/licenses/MIT")),
    watchSources ~= { _.filterNot(f =>
      f.getName.endsWith(".swp") || f.getName.endsWith(".swo") || f.isDirectory
    )},
    shellPrompt := { state =>
      val branch = if(file(".git").exists){
        "git branch".lines_!.find{_.head == '*'}.map{_.drop(1)}.getOrElse("")
      }else ""
      Project.extract(state).currentRef.project + branch + " > "
    },
    resolvers += Opts.resolver.sonatypeReleases,
    resolvers += Resolver.url("typesafe", url("http://repo.typesafe.com/typesafe/releases/"))(Resolver.ivyStylePatterns),
    libraryDependencies <+= sbtDependency,
    libraryDependencies ++= (
      ("com.chuusai" % "shapeless" % "2.0.0" cross CrossVersion.full) ::
      ("org.scalikejdbc" %% "scalikejdbc" % "2.1.0") ::
      Nil
    ) ++ scalaz ++ unfiltered ++ spire,
    libraryDependencies ~= {_.map(_.copy(configurations = Some("compile")))},
    libraryDependencies ~= {_.map(_.exclude("org.eclipse.jetty.orbit", "javax.servlet"))}
  )

}
