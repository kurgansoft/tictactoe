package tictactoe.shared

import gbge.shared.*
import zio.json.ast.Json
import zio.json.JsonCodec
import zio.schema.{DeriveSchema, Schema}

case class ClientTicTacToe(innerGame: Option[ClientInnerTicTacToe]) extends AbstractTicTacToe(innerGame) with FrontendGame[TicTacToeAction] {
  override lazy val encode: Json = ClientTicTacToe.codec.encoder.toJsonAST(this).getOrElse(???)
}

object ClientTicTacToe {
  implicit val schema: Schema[ClientTicTacToe] = DeriveSchema.gen[ClientTicTacToe]
  implicit val codec: JsonCodec[ClientTicTacToe] =
    zio.schema.codec.JsonCodec.jsonCodec(schema)
}
