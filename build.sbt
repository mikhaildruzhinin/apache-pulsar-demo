ThisBuild / scalaVersion     := "2.13.8"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

lazy val root = (project in file("."))
  .settings(
    name := "apache-pulsar-demo"
  )

val pulsar4sVersion = "2.7.3"

lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.2.12"
lazy val pulsar4s       = "com.sksamuel.pulsar4s" %% "pulsar4s-core"  % pulsar4sVersion
lazy val pulsar4sCirce  = "com.sksamuel.pulsar4s" %% "pulsar4s-circe" % pulsar4sVersion
lazy val typesafeConfig = "com.typesafe" % "config" % "1.4.2"

libraryDependencies ++= Seq(
  scalaTest,
  pulsar4s,
  pulsar4sCirce,
  typesafeConfig
)
