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
                       `type`:Int,
                       subtype:Int)

  case class Binder(uuid:String,
                    name:String)

  val parsedChecklist: JValue = parse(Source.fromResource("checklist.json").mkString)

  val blobject = parsedChecklist.extract[Blob]

  val itemMap = blobject.checklistItems map (t => t.uuid -> t) toMap

  def formatItems(filter:Checklist=>Boolean) = {
    for (cl <- blobject.checklists.filter(filter))
      yield
        div(p(cl.name),ul(for (cli <- cl.checklistItems) yield
          if (itemMap(cli).itemType == 11)
            p(s"${itemMap(cli).title} -- ${itemMap(cli).action}")
            else
            li(s"${itemMap(cli).title} : ${itemMap(cli).action}")))
  }

  def preflight(p:Checklist):Boolean = (p.subtype == 0) && (p.`type` == 0)
  def cruise(p:Checklist):Boolean = (p.subtype == 1) && (p.`type` == 0)
  def landing(p:Checklist):Boolean = (p.subtype == 2) && (p.`type` == 0)
  def abnormal(p:Checklist):Boolean = p.`type` == 1
  def emergency(p:Checklist):Boolean = p.`type` == 2

  def htmlChecklist =
    html(
      head(script("")),
      body(
        h1("C-177B Checklists"),
        div(
          h2("Normal"),
           h3("preflight"),
            formatItems(preflight),
          h3("Takeoff/cruise"),
            formatItems(cruise),
          h3("Landing"),
            formatItems(landing),
          h2("Abnormal"),
            formatItems(abnormal),
          h2("Emergency"),
            formatItems(emergency),
    )))

  def writeHtml = {
    val pw =new java.io.PrintWriter("htmlChecklist.html")
    pw.print(htmlChecklist.toString)
    pw.close
  }
}
