package xuwei_k.classdiagram

import scalaz._, syntax.std.list._
import play.api.mvc._

object QueryBinders {
  implicit def bindableList[T: QueryStringBindable] = new QueryStringBindable[NonEmptyList[T]] {
    def bind(key: String, params: Map[String, Seq[String]]) =
      bindList[T](key, params).toNel.map(Right(_))
    def unbind(key: String, values: NonEmptyList[T]) =
      unbindList(key, values.list)
  }

  // https://github.com/playframework/playframework/blob/2.1.3/framework/src/play/src/main/scala/play/api/mvc/Binders.scala#L410-L423
  private def bindList[T: QueryStringBindable](key: String, params: Map[String, Seq[String]]): List[T] = {
    for {
      values <- params.get(key).toList
      rawValue <- values
      bound <- implicitly[QueryStringBindable[T]].bind(key, Map(key -> Seq(rawValue)))
      value <- bound.right.toOption
    } yield value
  }

  private def unbindList[T: QueryStringBindable](key: String, values: Iterable[T]): String = {
    (for (value <- values) yield {
      implicitly[QueryStringBindable[T]].unbind(key, value)
    }).mkString("&")
  }
}
