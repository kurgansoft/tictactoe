package tictactoe.shared

import gbge.shared._
import upickle.default.{macroRW, ReadWriter => RW}

case class ClientTicTacToe(innerGame: Option[ClientInnerTicTacToe]) extends AbstractTicTacToe(innerGame) with FrontendGame[TicTacToeAction] {
  override def serialize(): String = {
    upickle.default.write(this)
  }

  override def decodeAction(payload: String): TicTacToeAction = {
    upickle.default.read[TicTacToeAction](payload)
  }
}

object ClientTicTacToe extends DecodeCapable {
  implicit def rw: RW[ClientTicTacToe] = macroRW

  override def decode(encodedForm: String): ClientTicTacToe = {
    upickle.default.read[ClientTicTacToe](encodedForm)
  }

  override val name: String = "TicTacToe"
}
