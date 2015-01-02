name := """alumni"""

version := "0.1.0"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

libraryDependencies ++= Seq(
  "com.adrianhurt" %% "play-bootstrap3" % "0.4-SNAPSHOT",
  "org.webjars" % "jquery" % "2.1.1",
  "org.webjars" % "bootstrap" % "3.3.1",
  javaJdbc,
  javaEbean
)
