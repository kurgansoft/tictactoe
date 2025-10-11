
ThisBuild / scalaVersion := "3.3.5"

val gbgeCommitHash = "b2b0a96c0b7bf5aa404ac083f6f846a1e30d5cb1"

lazy val common = crossProject(JSPlatform, JVMPlatform).in(file("common")).
  settings(
    name := "common",
    libraryDependencies ++= Seq(
      "com.github.kurgansoft" % "gbge" % gbgeCommitHash
    ),
    resolvers += "jitpack" at "https://jitpack.io"
  )
  .jvmSettings(
    target := file("target/jvm")
  )
  .jsSettings(
    target := file("target/js"),
    scalaJSUseMainModuleInitializer := false
  )

lazy val backend = project.in(file("backend")).settings(
  name := "backend",
).dependsOn(common.jvm)

lazy val ui = project.in(file("ui")).settings(
  name := "ui",
  Compile / scalaJSUseMainModuleInitializer:= false,
).dependsOn(common.js).enablePlugins(ScalaJSPlugin)