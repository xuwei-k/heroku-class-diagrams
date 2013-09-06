package controllers

import scalaz._
import play.api._
import play.api.mvc._
import xuwei_k.classDiagram
import xuwei_k.classDiagram.{SVG, DiagramService}
import xuwei_k.classDiagram.Front._

object Application extends Controller {

  private def default(allclass: Boolean, name: String) = {
    Ok(template(printClassList(name, allclass), name)).as(HTML)
  }

  def index(allclass: Option[String]) = Action{
    default(allclass.isDefined, "")
  }

  def list(classes: NonEmptyList[String]) = Action{
    val name = "class diagrams"
    val nodes = DiagramService.createClassDiagramByName(name)(classes.list: _*)
    val resource = SVG(nodes, name)
    Ok(resource.mkString).as(resource.contentType)
  }

  def diagrams(className: String, allclass: Option[String]) = Action {
    val isAllClass = allclass.isDefined
    val name = className.replace(".svg", "")

    createNodeList(name) match {
      case None =>
        default(isAllClass, name)
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
