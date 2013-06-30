import AssemblyKeys._

assemblySettings

name := "rock-collector"

organization := "com.bfritz"

version := "0.1.0-SNAPSHOT"

homepage := Some(url("https://github.com/bfritz/rock-collector"))

startYear := Some(2013)

scmInfo := Some(
  ScmInfo(
    url("https://github.com/bfritz/rock-collector"),
    "scm:git:https://github.com/bfritz/rock-collector.git",
    Some("scm:git:git@github.com:bfritz/rock-collector.git")
  )
)

/* scala versions and options */
scalaVersion := "2.10.1"

crossScalaVersions := Seq(
/*  "2.9.3-RC1",
  "2.9.2",
  "2.9.1", "2.9.1-1",
  "2.9.0", "2.9.0-1",
  "2.8.0", "2.8.1", "2.8.2" */
)

// These options will be used for *all* versions.
scalacOptions ++= Seq(
  "-deprecation"
  ,"-unchecked"
  ,"-encoding", "UTF-8"
  ,"-target:jvm-1.6"
  // "-optimise"   // this option will slow your build
)

scalacOptions ++= Seq(
  "-Yclosure-elim",
  "-Yinline"
)

// These language flags will be used only for 2.10.x.
// Uncomment those you need, or if you hate SIP-18, all of them.
scalacOptions <++= scalaVersion map { sv =>
  if (sv startsWith "2.10") List(
    "-Xverify"
    ,"-Ywarn-all"
    ,"-feature"
    ,"-language:postfixOps"
    // "-language:reflectiveCalls",
    // "-language:implicitConversions"
    // "-language:higherKinds",
    // "-language:existentials",
    // "-language:experimental.macros",
    // "-language:experimental.dynamics"
  )
  else Nil
}

javacOptions ++= Seq("-Xlint:unchecked", "-Xlint:deprecation")

/* dependencies */
libraryDependencies ++= Seq (
  "com.github.nscala-time" %% "nscala-time" % "0.4.2"
  // -- testing --
  , "org.scalatest" % "scalatest_2.10" % "2.0.M5b" % "test"
  , "org.scalamock" %% "scalamock-scalatest-support" % "3.0.1" % "test" 
  // -- Configuration --
  ,"com.typesafe" % "config" % "1.0.1"
  // -- Logging --
  ,"ch.qos.logback" % "logback-classic" % "1.0.13"
  ,"com.typesafe" %% "scalalogging-slf4j" % "1.0.1"
  // -- Netty --
  ,"io.netty" % "netty-handler" % "4.0.0.CR6"
  ,"io.netty" % "netty-codec-http" % "4.0.0.CR6"
  ,"org.javassist" % "javassist" % "3.18.0-GA"
)

/* you may need these repos */
resolvers ++= Seq(
  // Resolver.sonatypeRepo("snapshots")
  // Resolver.typesafeRepo("releases")
  // "spray repo" at "http://repo.spray.io"
)

/* assembly plugin */
mainClass in AssemblyKeys.assembly := Some("com.bfritz.rockcollector.GemServer")

test in AssemblyKeys.assembly := {}

net.virtualvoid.sbt.graph.Plugin.graphSettings
