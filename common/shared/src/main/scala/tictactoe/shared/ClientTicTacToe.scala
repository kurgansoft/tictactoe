package tictactoe.shared

import gbge.shared.*
import zio.json.ast.Json
import zio.json.{DeriveJsonCodec, JsonCodec}

case class ClientTicTacToe(innerGame: Option[ClientInnerTicTacToe]) extends AbstractTicTacToe(innerGame) with FrontendGame[TicTacToeAction] {
  override lazy val encode: Json = ClientTicTacToe.codec.encoder.toJsonAST(this).getOrElse(???)
}

object ClientTicTacToe {
  implicit val codec: JsonCodec[ClientTicTacToe] = DeriveJsonCodec.gen[ClientTicTacToe]
}
