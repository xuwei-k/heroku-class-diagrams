package xuwei_k.classDiagram

object URLMap{

  private[this] val SBT_SXR = "http://www.scala-sbt.org/0.12.2/sxr/"
  private[this] val GITHUB_SCALA = "https://github.com/scala/scala/blob/v2.10.1/src/"
  private[this] val LINE1 = ".scala#L1"
  private[this] val SCALAZ_GITHUB = "http://github.com/scalaz/scalaz/blob/v7.0.0-RC1/"

  def apply(name: String):String = {
    val fullName = name.split("""\$""").head
    val path = fullName.replace(".", "/")

    def s(prefix:String) = fullName.startsWith(prefix)
    // TODO use PartialFunction ?
    if(Seq("ant","cmd","nsc","reflect","util").exists{ p =>s("scala.tools." + p ) }
       || s("scala.reflect.runtime") || s("scala.reflect.internal") ){
      GITHUB_SCALA + "compiler/" + path + LINE1
    } else if (s("scala.tools.scalap") ){
      GITHUB_SCALA + "scalap/" + path + LINE1
    } else if (s("scala.actors")) {
       GITHUB_SCALA + "actors/" + path + LINE1
    } else if (s("scala.")) {
       GITHUB_SCALA + "library/" + path + LINE1
    } else if (s("java")) {
      "http://docs.oracle.com/javase/7/docs/api/" + path + ".html"
    } else if (s("org.jruby")){
      "http://www.jruby.org/apidocs/" + path + ".html"
    } else if (s("groovy")){
      "http://groovy.codehaus.org/api/" + path + ".html"
    } else if (s("scalaz")){
      val module = fullName.split('.')(1) match {
        case "concurrent" => "concurrent"
        case "example"    => "example"
        case "iteratee"   => "iteratee"
        case "scalacheck" => "scalacheck-binding"
        case "xml"        => "xml"
        case "typelevel"  => "typelevel"
        case _ =>
          if(fullName.contains("effect")) "effect"
          else "core"
      }
      SCALAZ_GITHUB + module + "/src/main/scala/" + path + ".scala"
    } else if (s("xsbt")){
      SBT_SXR + path + ".java"
    } else if (s("sbt.")){
      SBT_SXR + path.split("/").last + ".scala"
    } else if (s("specs2.") || s("org.specs2.")){
      "http://etorreborre.github.com/specs2/api/#" + fullName
    } else if (s("android.")){
      "http://developer.android.com/reference/" + path.replace('$','.') + ".html"
    } else ""
  }
}

