package checklist

import org.json4s._
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._
import scala.io.Source
import scalatags.Text.all._
import scalatags.stylesheet._


object Checklist extends App {
  implicit val formats = DefaultFormats

  case class Blob(
                   checklistItems: List[Item],
                   checklists: List[Checklist],
                   binders: List[Binder],
                   version: String
                 )

  case class Item(uuid: String,
                  action: String,
                  itemType: Int,
                  title: String)

  case class Checklist(uuid: String,
                       checklistItems: List[String],
                       name: String,
                       `type`: Int,
                       subtype: Int)

  def preflight(p: Checklist): Boolean = (p.subtype == 0) && (p.`type` == 0)

  def cruise(p: Checklist): Boolean = (p.subtype == 1) && (p.`type` == 0)

  def landing(p: Checklist): Boolean = (p.subtype == 2) && (p.`type` == 0)

  def other(p: Checklist): Boolean = (p.subtype == 3) && (p.`type` == 0)

  def abnormal(p: Checklist): Boolean = p.`type` == 1

  def emergency(p: Checklist): Boolean = p.`type` == 2

  case class Binder(checklists: List[String])

  val parsedChecklist: JValue = parse(Source.fromResource("checklist.json").mkString)
  val blobject = parsedChecklist.extract[Blob]
  val itemMap = blobject.checklistItems map (t => t.uuid -> t) toMap
  val checklistMap = blobject.checklists map (t => t.uuid -> t) toMap
  val C177Checklists = blobject.binders.head.checklists

  def divMultiCol(multi : Boolean) =
    if (multi) div(style := "border-style:solid; margin: 10px; column-break-inside:avoid; column-count:2; page-break-inside:avoid;")
    else div(style := "border-style:solid; margin: 10px; column-count:1; page-break-inside:avoid;")

  def formatItemsHtml(filter: Checklist => Boolean) = {
    for (cl <- C177Checklists.map(u => checklistMap(u)).filter(filter))
      yield
       divMultiCol(cl.name == "Preflight Inspection")(
          h4(style := "text-align: center")(cl.name),
          ul(
            for (cli <- cl.checklistItems) yield
            if (itemMap(cli).itemType == 11)
              p(s"${itemMap(cli).title} -- ${itemMap(cli).action}")
            else
              li(s"${itemMap(cli).title} : ${itemMap(cli).action}"))
        )
  }

  def htmlChecklist =
    html(
      // head(style := "@page :header {content : last(chapter)}", style :=  "H2 {running-head: chapter;}" ),
      head(style := ""),
      body(
        h1("C-177B N19762 Procedures"),
        p(style := "font-size:-2;font-style:italic")("from Margaret Leber's Garmin Pilot account"),
        div(style := "column-count:1;")(
          div(h2(style := "page-break-inside:avoid;")("Normal Procedures"),
            div(h3(style := "text-align: center;")("Preflight"),formatItemsHtml(preflight)),
            div(style := "page-break-inside:avoid;")(h3(style := "text-align: center;")("Takeoff/cruise"),formatItemsHtml(cruise)),
            div(style := "page-break-inside:avoid;")(h3(style := "text-align: center;")("Landing"),       formatItemsHtml(landing)),
            div(style := "page-break-inside:avoid;")(h3(style := "text-align: center;")("Other"),         formatItemsHtml(other))
          ),
          div(h2(style := "page-break-inside:avoid;")("Abnormal Procedures"),  formatItemsHtml(abnormal)),
          div(h2(style := "page-break-inside:avoid; page-break-before:always;")("Emergency Procedures"), formatItemsHtml(emergency))
        )))

  def writeHtml = {
    val pw = new java.io.PrintWriter("htmlChecklist.html")
    pw.print(htmlChecklist.toString)
    pw.close
  }


  /**
    *  ACE format:
    *  magic number-revision-CRLF
    *  checklistName CRLF
    *  aircraftMakeAndModel-CRLF
    *  aircraftInfo-CRLF (???)
    *  manufacturerIdentification-CRLF
    *  copyright-CRLF
    *  <N ... > delimits group (N indent) first group and checklist are default display: emergency
    *  (N ... ) delimits checklist (N indent)
    *  wN introduces WARNING
    *  aN introduces CAUTION
    *  nN introduces NOTE
    *  pN introduces plain text
    *  rN introduces challenge-response; tilde [~] separates response from challenge
    *  N can be 0,1,2,3,4 indent, C for centered
   */
  def aceChecklist = {
    val magic = 0xf0f0f0f0
    val revision = 0x00010000
    val checklistName = ""
    val checklistAircraftMakeAndModel = ""
    val checklistAircraftInfo = ""
    val checklistManufacturerIdentification = ""
    val checklistCopyright = ""


  }

  writeHtml
}
