import mill._
import mill.scalajslib.ScalaJSModule
import mill.scalalib._

object TicTacToe extends Module {
  val ___scalaVersion = "2.13.3"
  val ___scalaJSVersion = "1.4.0"

  object shared extends Module {
    def scalaVersion = ___scalaVersion
    def scalaJSVersion = ___scalaJSVersion

    trait Common extends ScalaModule {
      override def millSourcePath = shared.millSourcePath
      override def scalaVersion = ___scalaVersion
      override def ivyDeps = Agg(
        ivy"com.kurgansoft::gbgeShared::0.0.1"
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
      ivy"com.kurgansoft::gbgeBackend:0.0.1"
    )

    override def mainClass = T(Some("base.StandardLauncher"))

    object test extends Tests {
      override def ivyDeps = Agg(
        ivy"org.scalatest::scalatest:3.1.2",
        ivy"org.scalacheck::scalacheck:1.14.1"
      )
      def testFrameworks = Seq("org.scalatest.tools.Framework")
    }
  }

  object ui extends ScalaJSModule {
    def scalaVersion = ___scalaVersion
    def scalaJSVersion = ___scalaJSVersion
    override def moduleDeps = Seq(shared.js)
    override def scalacOptions = Seq("-Xxml:-coalescing")

    override def ivyDeps = Agg(
      ivy"com.kurgansoft:gbgeUI_sjs1_2.13:0.0.1"
    )
  }
}