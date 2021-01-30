package controllers

import play.api.mvc._
import xuwei_k.classDiagram
import xuwei_k.classDiagram.SVG
import xuwei_k.classDiagram.Front._
import javax.inject.Inject

class Application @Inject() (cc: ControllerComponents) extends AbstractController(cc) {

  def index(allclass: Option[String]) = Action{
    Ok(template(printClassList("",allclass.isDefined),"class diagrams")).as(HTML)
  }

  def diagrams(className: String, allclass: Option[String]) = Action {
    val isAllClass = allclass.isDefined
    val name = className.replace(".svg", "")

    createNodeList(name) match {
      case None =>
        Ok(template(printClassList(name,isAllClass),name)).as(HTML)
      case Some(nodes) => {
        val resource =
          if(className.endsWith(".svg")){
            SVG(nodes, name)
          }else{
            classDiagram.HTML(nodes,name)
          }

        Ok(resource.mkString).as(resource.contentType)
      }
    }
  }

}
