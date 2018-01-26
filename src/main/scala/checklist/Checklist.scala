package checklist

import org.json4s._
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._
import scala.io.Source
import scalatags.Text.all._


object Checklist {
   implicit val formats = DefaultFormats

   case class Blob(binders:List[Binder],
                  checklists:List[Checklist],
                  checklistItems:List[Item],
                  version:String)

  case class Item(uuid:String,
                  action:String,
                  itemType:Int,
                  title:String)

  case class Checklist(uuid:String,
                       checklistItems:List[String],
                       name:String,
                       subtype:Int)

  case class Binder(uuid:String,
                    name:String)

  val parsedChecklist: JValue = parse(Source.fromResource("checklist.json").mkString)

  val blobject = parsedChecklist.extract[Blob]

  val itemMap = blobject.checklistItems map (t => t.uuid -> t) toMap

  def htmlChecklist =
    html(
      head(script("")),
      body(
        h1("C-177B Checklists"),
        div(
          p("This is my first paragraph"),
          for (c <- blobject.checklists) yield p(c.name)
        )
      )
    )

  def writeHtml = {
    val pw =new java.io.PrintWriter("htmlChecklist")
    pw.print(htmlChecklist.toString)
    pw.close
  }
}
