import sbt._,Keys._

object build extends Build {

  val originalJvmOptions = sys.process.javaVmArguments.filter(
    a => Seq("-Xmx", "-Xms", "-XX").exists(a.startsWith)
  )

  val lift = Seq(
    "common","json","actor","util","json-scalaz","json-ext"
  ).map{n => "net.liftweb" %% ("lift-" + n ) % "2.5.1"}

  val unfiltered = Seq(
    "filter","filter-async","agents","uploads","util","jetty","jetty-ajp","netty-server",
    "netty","json4s","netty-websockets","oauth","spec","scalatest","directives"
  ).map{n => "net.databinder" %% ("unfiltered-" + n ) % "0.6.8"}

  val scalaz = Seq(
    "core","concurrent","effect","iteratee","scalacheck-binding","typelevel"
  ).map{ m =>
    "org.scalaz" %% ("scalaz-" + m) % "7.1.0-M3"
  }

  val spire = Seq("spire", "spire-scalacheck-binding").map("org.spire-math" %% _ % "0.5.1")

  val main = play.Project(
    "heroku-class-diagrams", "0.1-SNAPSHOT", Nil
  ).settings(
    scalaVersion := "2.10.3-RC3",
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
    PlayKeys.routesImport ++= Seq(
      "scalaz.NonEmptyList", "xuwei_k.classdiagram.QueryBinders._"
    ),
    resolvers += Opts.resolver.sonatypeReleases,
    resolvers += Resolver.url("typesafe", url("http://repo.typesafe.com/typesafe/releases/"))(Resolver.ivyStylePatterns),
    libraryDependencies += "org.scala-sbt" % "sbt" % "0.13.0-RC4",
    libraryDependencies ++= Seq(
      "com.chuusai" %% "shapeless" % "1.2.4",
      "com.github.seratch" %% "scalikejdbc-interpolation" % "1.6.7",
      "org.squeryl" %% "squeryl" % "0.9.6-RC1",
      "postgresql" % "postgresql" % "9.1-903.jdbc4" from "http://jdbc.postgresql.org/download/postgresql-9.1-903.jdbc4.jar",
      "mysql" % "mysql-connector-java" % "5.1.25",
      "net.sf.barcode4j" % "barcode4j" % "2.1",
      "org.fusesource.scalate" %% "scalate-core" % "1.6.1",
      "com.github.kmizu" %% "jsonda-json4s" % "0.8.0",
      "net.sf.opencsv" % "opencsv" % "2.3",
      "jmimemagic" % "jmimemagic" % "0.1.2",
      "jmagick" % "jmagick" % "6.2.4",
      "com.typesafe" %% "play-plugins-mailer" % "2.1.0",
      "com.github.nscala-time" %% "nscala-time" % "0.4.2",
      "org.scalaj" %% "scalaj-http" % "0.3.9" exclude("junit", "junit"),
      "org.mongodb" %% "casbah-core" % "2.6.2"
    ) ++ scalaz ++ unfiltered ++ lift ++ spire,
    libraryDependencies ~= {_.map(_.copy(configurations = Some("compile")))},
    libraryDependencies ~= {_.map(_.exclude("org.eclipse.jetty.orbit", "javax.servlet"))}
  )

}
