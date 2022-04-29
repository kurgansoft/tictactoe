import coursier.{MavenRepository, Repository}
import mill._
import mill.define.Task
import mill.scalajslib.ScalaJSModule
import mill.scalalib._

object TicTacToe extends Module {
  val ___scalaVersion = "2.13.8"
  val ___scalaJSVersion = "1.10.0"

  val gbgeVersion: String = "0.2.1"

  object shared extends Module {
    def scalaVersion = ___scalaVersion
    def scalaJSVersion = ___scalaJSVersion

    trait Common extends ScalaModule {
      override def millSourcePath = shared.millSourcePath
      override def scalaVersion = ___scalaVersion
      override def ivyDeps = Agg(
        ivy"com.kurgansoft::gbgeShared::$gbgeVersion"
      )
    }

    object jvm extends ScalaModule with Common

    object js extends ScalaJSModule with Common {
      override def scalaJSVersion = ___scalaJSVersion
    }

  }

  object backend extends ScalaModule {
    def scalaVersion = ___scalaVersion
    override def moduleDeps = Seq(shared.jvm)

    override def ivyDeps = Agg(
      ivy"com.kurgansoft::gbgeBackend:$gbgeVersion"
    )

    override def mainClass = T(Some("base.StandardLauncher"))

    object test extends Tests {
      override def ivyDeps = Agg(
        ivy"org.scalatest::scalatest:3.1.2",
        ivy"org.scalacheck::scalacheck:1.14.1"
      )
      override def testFramework = "org.scalatest.tools.Framework"
    }
  }

  object ui extends ScalaJSModule {
    def scalaVersion = ___scalaVersion
    def scalaJSVersion = ___scalaJSVersion
    override def moduleDeps = Seq(shared.js)
    override def scalacOptions = Seq("-Xxml:-coalescing")

    override def repositoriesTask: Task[Seq[Repository]] = T.task {
      super.repositoriesTask() ++ Seq(
        MavenRepository("https://jitpack.io")
      )
    }

    override def ivyDeps = Agg(
      ivy"com.kurgansoft:gbgeUI_sjs1_2.13:$gbgeVersion"
    )
  }
}