package tictactoe.shared

import gbge.shared.actions.GameAction
import zio.blocks.schema.Schema
import zio.blocks.schema.json.JsonCodec

sealed trait TicTacToeAction extends GameAction {
  override lazy val asJsonString: String = TicTacToeAction.jsonCodec.encodeToString(this)
}

object TicTacToeAction {
  val jsonCodec: JsonCodec[TicTacToeAction] = Schema.derived.jsonCodec
}

case object Init extends TicTacToeAction {
  override val adminOnly: Boolean = true
}
case object Restart extends TicTacToeAction {
  override val adminOnly: Boolean = true
}

case class TakeMark(index: Int) extends TicTacToeAction
