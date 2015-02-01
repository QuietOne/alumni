name := """alumni"""

version := "0.3.0"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

libraryDependencies ++= Seq(
  "org.webjars" % "jquery" % "2.1.1",
  "org.webjars" % "bootstrap" % "3.3.1",
  javaJdbc,
  javaEbean,
  javaWs
)
