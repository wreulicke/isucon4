name := "scala-play-scalikejdbc"

version := "0.1.0-SNAPSHOT"

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)

scalaVersion := "2.11.11"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,

  "org.scalikejdbc" %% "scalikejdbc" % "2.5.0",
  "org.scalikejdbc" %% "scalikejdbc-config" % "2.5.0",
  "org.scalikejdbc" %% "scalikejdbc-jsr310" % "2.5.0",
  "org.scalikejdbc" %% "scalikejdbc-play-initializer" % "2.5.1",
  "org.scalikejdbc" %% "scalikejdbc-play-dbapi-adapter" % "2.5.1",
  "mysql" % "mysql-connector-java" % "5.1.40",
  "com.typesafe.scala-logging" %% "scala-logging-slf4j" % "2.1.2",

  "org.flywaydb" %% "flyway-play" % "3.0.1",

  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test
)
