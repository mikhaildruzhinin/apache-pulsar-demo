import Dependencies._

ThisBuild / scalaVersion     := "2.13.8"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

lazy val root = (project in file("."))
  .settings(
    name := "apache-pulsar-demo",
    libraryDependencies += scalaTest % Test
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.

val pulsar4sVersion = "2.7.3"

lazy val pulsar4s       = "com.sksamuel.pulsar4s" %% "pulsar4s-core"  % pulsar4sVersion
lazy val pulsar4sCirce  = "com.sksamuel.pulsar4s" %% "pulsar4s-circe" % pulsar4sVersion
lazy val typesafeConfig = "com.typesafe" % "config" % "1.4.2"

libraryDependencies ++= Seq(
  pulsar4s,
  pulsar4sCirce,
  typesafeConfig
)
