package checklist

import org.json4s._
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._
import scala.io.Source
import scalatags.Text.all._
import scalatags.stylesheet._
import java.util.zip.CRC32
import scala.math.BigInt


object Checklist extends App {
  implicit val formats = DefaultFormats
  val CRLF = "\r\n"

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

  def divMultiCol(multi: Boolean) =
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
            div(h3(style := "text-align: center;")("Preflight"), formatItemsHtml(preflight)),
            div(style := "page-break-inside:avoid;")(h3(style := "text-align: center;")("Takeoff/cruise"), formatItemsHtml(cruise)),
            div(style := "page-break-inside:avoid;")(h3(style := "text-align: center;")("Landing"), formatItemsHtml(landing)),
            div(style := "page-break-inside:avoid;")(h3(style := "text-align: center;")("Other"), formatItemsHtml(other))
          ),
          div(h2(style := "page-break-inside:avoid;")("Abnormal Procedures"), formatItemsHtml(abnormal)),
          div(h2(style := "page-break-inside:avoid; page-break-before:always;")("Emergency Procedures"), formatItemsHtml(emergency))
        )))


  // def clDump(cl:Checklist) = {println(s"checklist ${cl.name} (${cl.`type`},${cl.subtype})CRLF");cl}

  def formatListAce(filter: Checklist => Boolean) = {
    val CRLF = "\r\n"
    C177Checklists.map(u => checklistMap(u)).filter(filter).map(cl => s"(0${cl.name}$CRLF" + formatItemsAce(cl.checklistItems).mkString(CRLF) + CRLF + ")")
  }

  def formatItemsAce(items: List[String]) = {
    items.map(
      cli =>
        if (itemMap(cli).itemType == 11)
          s"n0${itemMap(cli).title}~${itemMap(cli).action}"
        else
          s"r0${itemMap(cli).title}~${itemMap(cli).action}"
    )
  }

  def minimalChecklist:String = {
  val magicAsString = "\u00f0\u00f0\u00f0\u00f0"
  val revisionAsString = "\u0000\u0001\u0000\u0000"
  val minimal =
    magicAsString + revisionAsString + CRLF +
      "GARMIN CHECKLIST PN XXX-XXXXX-XX" + CRLF +
      "Aircraft Make and Model" + CRLF +
      "Aircraft specific identification" + CRLF +
      "Manufacturer Identification" + CRLF +
      "Copyright Information" + CRLF +
  "<0The group" + CRLF +
    "(0The Checklist" + CRLF +
    "c0The Entry" + CRLF +
  ")" + CRLF +
    ">" + CRLF +
    "END" + CRLF

    val crc = new CRC32
    crc.update(minimal.getBytes())
    // BigInt(crc.getValue).toByteArray.reverse.map( ~ _).map(_ & 0xff).map(_.toHexString)
    val crcInts:Array[Int] = BigInt(crc.getValue).toByteArray.reverse.map( ~ _).map(_ & 0xff)
    return minimal + crcInts(0).asInstanceOf[Char] + crcInts(1).asInstanceOf[Char]+ crcInts(2).asInstanceOf[Char]+ crcInts(3).asInstanceOf[Char]
  }

  /**
    *
    * ACE format:
    * magic number-revision-CRLF
    * checklistName CRLF
    * aircraftMakeAndModel-CRLF
    * aircraftInfo-CRLF (???)
    * manufacturerIdentification-CRLF
    * copyright-CRLF
    * <N ... > delimits group (N indent) first group and checklist are default display: emergency
    * (N ... ) delimits checklist (N indent)
    * wN introduces WARNING
    * aN introduces CAUTION
    * nN introduces NOTE
    * pN introduces plain text
    * rN introduces challenge-response; tilde [~] separates response from challenge
    * N can be 0,1,2,3,4 indent, C for centered
    */

  def aceChecklist: String = {
    val magicAsString = "\u00f0\u00f0\u00f0\u00f0"
    val revisionAsString = "\u0000\u0001\u0000\u0000"
    val name = "C177B Procedures"
    val aircraftMakeAndModel = "Cessna C177B"
    val aircraftInfo = "AircraftInfo"
    val manufacturerIdentification = "Cessna"
    val copyright = "no copyright just yet"


    def aceGroup(label: String, in: String) = s"<0$label$CRLF$in$CRLF>$CRLF"



    val ace =
      magicAsString + revisionAsString + CRLF +
        name + CRLF +
        aircraftMakeAndModel + CRLF +
        aircraftInfo + CRLF +
        manufacturerIdentification + CRLF +
        copyright + CRLF +
        //       aceGroup("Emergency", formatListAce(emergency).mkString(CRLF)) + CRLF +
//        aceGroup("Cruise", formatListAce(cruise).mkString(CRLF)) + CRLF +
//        aceGroup("Landing", formatListAce(landing).mkString(CRLF)) + CRLF +
//        aceGroup("Other", formatListAce(other).mkString(CRLF)) + CRLF +
       aceGroup("Abnormal", formatListAce(abnormal).mkString(CRLF)) + CRLF +
        "END" + CRLF

    val crc = new CRC32
    crc.update(ace.getBytes())
    val crcInts:Array[Int] = BigInt(crc.getValue).toByteArray.reverse.map( ~ _).map(_ & 0xff)
    return ace + crcInts(0).asInstanceOf[Char] + crcInts(1).asInstanceOf[Char]+ crcInts(2).asInstanceOf[Char]+ crcInts(3).asInstanceOf[Char]

  }

  def writeHtml = {
    val pw = new java.io.PrintWriter("htmlChecklist.html")
    pw.print(htmlChecklist.toString)
    pw.close
  }

  def writeAce = {
    val pw = new java.io.PrintWriter("C177checklist.ace")
    pw.print(aceChecklist)
    pw.close
  }

  def writeMinimal = {
    val pw = new java.io.PrintWriter("hackedminimal.ace")
    pw.print(minimalChecklist)
    pw.close
  }

  writeHtml
  writeAce
//  writeMinimal
}
