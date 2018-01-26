import checklist.Checklist
import org.json4s._
import org.json4s.BigDecimalJsonFormats
implicit val formats = DefaultFormats
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._
import scala.io.Source
import scalatags.Text.all._
import scalatags.DataConverters._
import checklist.Checklist._



val blob = Checklist.mockChecklistBlob
val checklists = (blob \ "checklists")
checklists
val firstCList = checklists.children.head
case class Clist(uuid: String, name:String)
val holyShit = firstCList.extract[Clist]

val blobject = blob.extract[Checklist.Blob]
blobject.checklists(0).checklistItems(0)

holyShit.name
html(body(p((holyShit.name))))
