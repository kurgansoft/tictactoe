package tictactoe.shared

import gbge.shared.*
import zio.blocks.schema.Schema
import zio.blocks.schema.json.{Json, JsonCodec}

case class ClientTicTacToe(innerGame: Option[ClientInnerTicTacToe])
    extends AbstractTicTacToe(innerGame)
    with FrontendGame[TicTacToeAction] {
  override lazy val encode: Json = ClientTicTacToe.jsonCodec.encodeToJson(this)
}

object ClientTicTacToe {
  val jsonCodec: JsonCodec[ClientTicTacToe] = Schema.derived.jsonCodec
}
