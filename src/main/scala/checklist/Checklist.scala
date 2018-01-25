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

  val nullChecklist =

    ("checklistItems" -> List()) ~
//      "checklistItems": [
//  {
//    "uuid": "d1182e1f-0bb6-4dc8-a363-1b1443b429fb",
//    "version": "MjAxNy0wNy0yNFQwOTo1Mzo0OC44MjJa",
//    "deletedAt": null,
//    "createdAt": "2017-07-24T09:52:29.342Z",
//    "updatedAt": "2017-07-24T09:52:29.342Z",
//    "action": "No Symptoms",
//    "itemType": 0,
//    "title": "Illness"
//  },
    ("checklists" -> List()) ~
//      "checklists": [
//  {
//    "uuid": "51650611-2639-4da3-9de4-fa2d5639d26d",
//    "version": "MjAxNy0wNy0yNFQwOTo1Mzo0OC44MTFa",
//    "deletedAt": null,
//    "createdAt": "2017-07-24T09:52:29.344Z",
//    "updatedAt": "2017-07-24T09:52:29.344Z",
//    "checklistItems": [
//    "d1182e1f-0bb6-4dc8-a363-1b1443b429fb",
//    "f70df93b-e0a6-4297-9fe4-81e2276523d7",
//    "edefb3a8-bd5c-4f34-af73-060b3dbec618",
//    "4dbad0d0-9782-4d29-a463-8dc163cafbb1",
//    "6c288d94-1364-4a81-b50b-49b17892a24a",
//    "854bdcb1-cfb3-41a5-81e2-87dd3a268d83"
//    ],
//    "completionItem": 1,
//    "name": "I'M SAFE",
//    "subtype": 3,
//    "type": 0
//  },
    ("binders" -> List()) ~
//      "binders": [
//  {
//    "uuid": "d9fa45be-16f8-43b9-a714-dbdc60c5b662",
//    "version": "MjAxNy0wOC0wNVQwMjoyODo0Ny4yNTha",
//    "deletedAt": null,
//    "createdAt": "2017-07-24T09:52:29.347Z",
//    "updatedAt": "2017-08-05T02:26:55.475Z",
//    "checklists": [
//    "51650611-2639-4da3-9de4-fa2d5639d26d"
//    ],
//    "name": "I'M SAFE",
//    "sortOrder": 9,
//    "sourceTemplateUUID": "b70f4410-ced1-4d30-84e1-68858cbe768b"
//  },
    ("version" -> "")


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

  def foo: JValue = {
    // parse(Source.fromFile("D:\\Users\\maggie\\Documents\\checklist\\src\\test\\resources\\checklist.json").mkString)
    parse(Source.fromResource("checklist.json").mkString)
  }
}
