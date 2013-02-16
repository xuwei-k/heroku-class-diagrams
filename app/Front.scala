package xuwei_k.classDiagram

import scala.xml.{Elem,Node}
import scala.util.control.Exception.catching

object Front {
  val ALL_CLASS = "allclass"

  def createNodeList[A](className: String): Option[List[Node]] = {
    catching(classOf[ClassNotFoundException]).opt{
      (Class.forName(className).asInstanceOf[Class[A]])
    }.map{ clazz =>
      DiagramService.createClassDiagramByClass(className)(clazz)
    }
  }

  def printClassList(prefix:String,containsAll:Boolean):Elem = {
    val list = JarExtractor.getClassNames(prefix.replace('.','/'),containsAll)

    <div>
    <p><span style="font-size:x-large;">{list.size}</span> classes</p>
    <p>{
      if(containsAll){
        <a href={"/"+prefix }>change only outer classes</a>
      }else{
        <a href={"/"+prefix+"?"+ALL_CLASS}>change all classes(contain inner classes)</a>
      }
    }</p>
    <ul>{list.map{ name =>
      <li><a href={ name } >{name}</a><a class={SVG_LINK} href={ name + ".svg"}>SVG</a></li>
    }}</ul>
    </div>
  }

  val SVG_LINK = "svglink"

  val doctype = "<!DOCTYPE html>"

  val STYLE = """ a.""" + SVG_LINK + """{ margin-left:30px; } """

  def template(body:Elem,title:String):Elem = {
<html lang="en">
  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <meta name="robots" content="noindex,nofollow" />
    <title>{title}</title>
    <link rel="shortcut icon" href="./favicon.ico" />
    <script type="text/javascript" src="https://apis.google.com/js/plusone.js"></script>
    <style type="text/css">
      { STYLE }
    </style>
  </head>
  <body>
    <p>{ HTML.tweetButton("#scala") }{ HTML.googlePlusOne }<a href="https://github.com/xuwei-k/heroku-class-diagrams">source code</a>
      Developing by <a href="http://twitter.com/xuwei_k" target="_blank">xuwei_k</a>
    </p>
    {body}
  </body>
</html>
  }
}
