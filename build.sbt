
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
  mainClass := Some("tictactoe.backend.launchers.Launcher"),
  assemblyMergeStrategy := {
    case x if Assembly.isConfigFile(x) =>
      MergeStrategy.concat
    case PathList(ps @ _*) if Assembly.isReadme(ps.last) || Assembly.isLicenseFile(ps.last) =>
      MergeStrategy.rename
    case PathList("META-INF", xs @ _*) =>
      xs map {_.toLowerCase} match {
        case "manifest.mf" :: Nil | "index.list" :: Nil | "dependencies" :: Nil =>
          MergeStrategy.discard
        case ps @ x :: xs if ps.last.endsWith(".sf") || ps.last.endsWith(".dsa") =>
          MergeStrategy.discard
        case "plexus" :: xs =>
          MergeStrategy.discard
        case "services" :: xs =>
          MergeStrategy.filterDistinctLines
        case "spring.schemas" :: Nil | "spring.handlers" :: Nil =>
          MergeStrategy.filterDistinctLines
        case _ => MergeStrategy.first
      }
    case other if other.endsWith(".sjsir") || other.contains("_sjs1") =>
      MergeStrategy.discard
    case _ => MergeStrategy.first
  }
).dependsOn(common.jvm)

lazy val ui = project.in(file("ui")).settings(
  name := "ui",
  Compile / scalaJSUseMainModuleInitializer:= false,
).dependsOn(common.js).enablePlugins(ScalaJSPlugin)