package tictactoe.shared

import gbge.shared.actions.GameAction
import zio.json.{JsonCodec, EncoderOps}
import zio.schema.{DeriveSchema, Schema}

sealed trait TicTacToeAction extends GameAction {
  override def convertToJson(): String =
    this.toJson
}

object TicTacToeAction {
  implicit val schema: Schema[TicTacToeAction] = DeriveSchema.gen
  implicit val codec: JsonCodec[TicTacToeAction] =
    zio.schema.codec.JsonCodec.jsonCodec(schema)
}

case object Init extends TicTacToeAction {
  override val adminOnly: Boolean = true
}
case object Restart extends TicTacToeAction {
  override val adminOnly: Boolean = true
}

case class TakeMark(index: Int) extends TicTacToeAction
