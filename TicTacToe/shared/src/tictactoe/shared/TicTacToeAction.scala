package tictactoe.shared

import gbge.shared.actions.GameAction
import upickle.default.{macroRW, ReadWriter => RW}

abstract sealed class TicTacToeAction extends GameAction {
  override def serialize(): String = upickle.default.write[TicTacToeAction](this)
}

object TicTacToeAction {
  implicit def rw: RW[TicTacToeAction] = macroRW
}

object Init extends TicTacToeAction {
  override val adminOnly: Boolean = true
}
object Restart extends TicTacToeAction {
  override val adminOnly: Boolean = true
}

case class TakeMark(index: Int) extends TicTacToeAction

object TakeMark {
  implicit def rw: RW[TakeMark] = macroRW
}