package checklist

import org.json4s._
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._

import scala.io.Source
import scalatags.Text.all._
object Checklist {
  def hellopage =
    html(
      head( script("some script") ),
      body(
        h1("This is my title"),
        div(
          p("This is my first paragraph"),
          p("This is my second paragraph")
        )
      )
    )

  val mockChecklist =

  def htmlChecklist =
    html(
      head( script("some script") ),
      body(
        h1("This is my title"),
        div(
          p("This is my first paragraph"),
          p("This is my second paragraph")
        )
      )
    )

  def foo:JValue = {
    // parse(Source.fromFile("D:\\Users\\maggie\\Documents\\checklist\\src\\test\\resources\\checklist.json").mkString)
    parse(Source.fromResource("checklist.json").mkString)
  }
}
