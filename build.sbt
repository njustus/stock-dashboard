val Http4sVersion = "0.23.8"
val CirceVersion = "0.14.1"
val LogbackVersion = "1.2.10"

lazy val root = (project in file("."))
  .settings(
    fork := true,
    connectInput := true,
    organization := "com.github.njustus",
    name := "stock-dashboard",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "3.1.0",
    libraryDependencies ++= Seq(
      "org.http4s"      %% "http4s-ember-server" % Http4sVersion,
      "org.http4s"      %% "http4s-ember-client" % Http4sVersion,
      "org.http4s"      %% "http4s-circe"        % Http4sVersion,
      "org.http4s"      %% "http4s-dsl"          % Http4sVersion,
      "io.circe"        %% "circe-generic"       % CirceVersion,
      "io.circe"        %% "circe-parser"       % CirceVersion,
      "io.circe" %% "circe-yaml" % CirceVersion,
      "de.neuland-bfi" % "pug4j" % "2.0.5",
      "ch.qos.logback"  %  "logback-classic"     % LogbackVersion,
      "com.typesafe.scala-logging" %% "scala-logging" % "3.9.4",
      "org.scalatest" %% "scalatest" % "3.2.9" % Test
    )
  )
