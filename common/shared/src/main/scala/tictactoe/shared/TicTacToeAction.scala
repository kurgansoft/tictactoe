package tictactoe.shared

import gbge.shared.actions.GameAction
import zio.json.{DeriveJsonCodec, JsonCodec}
import zio.schema.{DeriveSchema, Schema}

sealed trait TicTacToeAction extends GameAction

object TicTacToeAction {
  implicit val schema: Schema[TicTacToeAction] = DeriveSchema.gen

  implicit val codec: JsonCodec[TicTacToeAction] =
    DeriveJsonCodec.gen[TicTacToeAction]
}

case object Init extends TicTacToeAction {
  override val adminOnly: Boolean = true
}
case object Restart extends TicTacToeAction {
  override val adminOnly: Boolean = true
}

case class TakeMark(index: Int) extends TicTacToeAction
