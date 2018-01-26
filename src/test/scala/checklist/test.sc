import checklist.Checklist
import org.json4s._
import org.json4s.BigDecimalJsonFormats
implicit val formats = DefaultFormats
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._
import scala.io.Source
import scalatags.Text.all._
import scalatags.DataConverters._


val blob = Checklist.mockChecklistBlob
val checklists = (blob \ "checklists")
checklists
val firstCList = checklists.children.head
case class Clist(uuid: String, name:String)
val holyShit = firstCList.extract[Clist]
holyShit.name
html(body(p(())))
