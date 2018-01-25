package checklist

import org.json4s._
import org.json4s.JsonDSL._
// import org.json4s.scalap._
import org.json4s.native.JsonMethods._

import scala.io.Source
import scalatags.Text.all._

object Checklist {
  def hellopage =
    html(
      head(script("some script")),
      body(
        h1("This is my title"),
        div(
          p("This is my first paragraph"),
          p("This is my second paragraph")
        )
      )
    )

  val mockChecklistBlob =

    ("checklistItems" -> List(mockItem)) ~
    ("checklists" -> List(mockChecklist)) ~
    ("binders" -> List(mockBinder)) ~
    ("version" -> "")

  def mockItem : JValue = parse(
    """
 {
   "uuid": "d1182e1f-0bb6-4dc8-a363-1b1443b429fb",
   "version": "MjAxNy0wNy0yNFQwOTo1Mzo0OC44MjJa",
   "deletedAt": null,
   "createdAt": "2017-07-24T09:52:29.342Z",
   "updatedAt": "2017-07-24T09:52:29.342Z",
   "action": "No Symptoms",
   "itemType": 0,
   "title": "Illness"
 }
    """)

  def mockChecklist : JValue = parse(
    """
 {
   "uuid": "51650611-2639-4da3-9de4-fa2d5639d26d",
   "version": "MjAxNy0wNy0yNFQwOTo1Mzo0OC44MTFa",
   "deletedAt": null,
   "createdAt": "2017-07-24T09:52:29.344Z",
   "updatedAt": "2017-07-24T09:52:29.344Z",
   "checklistItems": [
   "d1182e1f-0bb6-4dc8-a363-1b1443b429fb"
   ],
   "completionItem": 1,
   "name": "I'M SAFE",
   "subtype": 3,
   "type": 0
 }
      """
  )

  def mockBinder = parse("""
     {
      "uuid": "d9fa45be-16f8-43b9-a714-dbdc60c5b662",
      "version": "MjAxNy0wOC0wNVQwMjoyODo0Ny4yNTha",
      "deletedAt": null,
      "createdAt": "2017-07-24T09:52:29.347Z",
      "updatedAt": "2017-08-05T02:26:55.475Z",
      "checklists": [
      "51650611-2639-4da3-9de4-fa2d5639d26d"
      ],
      "name": "I'M SAFE",
      "sortOrder": 9,
      "sourceTemplateUUID": "b70f4410-ced1-4d30-84e1-68858cbe768b"
    } """
  )

  def htmlChecklist =
    html(
      head(script("some script")),
      body(
        h1("This is my title"),
        div(
          p("This is my first paragraph"),
          p("This is my second paragraph")
        )
      )
    )

  def parsedChecklist: JValue = {
    parse(Source.fromResource("checklist.json").mkString)
  }
}
