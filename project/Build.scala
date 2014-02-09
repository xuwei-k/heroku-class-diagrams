import sbt._,Keys._

object build extends Build {

  val originalJvmOptions = sys.process.javaVmArguments.filter(
    a => Seq("-Xmx", "-Xms", "-XX").exists(a.startsWith)
  )

  val lift = Seq(
    "common","json","actor","util","json-scalaz","json-ext"
  ).map{n => "net.liftweb" %% ("lift-" + n ) % "2.6-M2"}

  val unfiltered = Seq(
    "filter","filter-async","agents","uploads","util","jetty","jetty-ajp","netty-server",
    "netty","json4s","netty-websockets","oauth","spec","scalatest","directives"
  ).map{n => "net.databinder" %% ("unfiltered-" + n ) % "0.7.1"}

  val scalaz = Seq(
    "core","concurrent","effect","iteratee","scalacheck-binding","typelevel"
  ).map{ m =>
    "org.scalaz" %% ("scalaz-" + m) % "7.1.0-M5"
  }

  val spire = Seq("spire", "spire-scalacheck-binding").map("org.spire-math" %% _ % "0.6.1")

  val main = play.Project(
    "heroku-class-diagrams", "0.1-SNAPSHOT", Nil
  ).settings(
    scalaVersion := "2.10.4-RC1",
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
    libraryDependencies ++= Seq(
      "com.chuusai" %% "shapeless" % "1.2.4",
      "org.scalikejdbc" %% "scalikejdbc-interpolation" % "1.7.1",
      "org.squeryl" %% "squeryl" % "0.9.6-RC2",
      "org.fusesource.scalate" %% "scalate-core" % "1.6.1",
      "com.github.kmizu" %% "jsonda-json4s" % "0.8.0",
      "com.github.nscala-time" %% "nscala-time" % "0.6.0",
//      "org.specs2" %% "specs2" % "2.2.2-scalaz-7.1.0-M3",
      "org.scalaj" %% "scalaj-http" % "0.3.12"
    ) ++ scalaz ++ unfiltered ++ lift ++ spire,
    libraryDependencies ~= {_.map(_.copy(configurations = Some("compile")))},
    libraryDependencies ~= {_.map(_.exclude("org.eclipse.jetty.orbit", "javax.servlet"))}
  )

}
