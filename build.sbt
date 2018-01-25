name := "checklist"

version := "0.1"

scalaVersion := "2.12.4"

libraryDependencies ++= Seq(
  "com.github.finagle" %% "finch-core" % "0.15.1",
  "com.github.finagle" %% "finch-json4s" % "0.15.1",
  "com.lihaoyi" %% "scalatags" % "0.6.7",
  "ch.qos.logback" % "logback-classic" % "1.1.7",
  "org.json4s" % "json4s-native_2.12" % "3.5.3",
  "org.json4s" % "json4s-ext_2.12" % "3.5.3"
)
