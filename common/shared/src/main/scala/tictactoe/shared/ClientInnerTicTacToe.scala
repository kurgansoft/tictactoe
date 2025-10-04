package tictactoe.shared

import zio.json.{DeriveJsonCodec, JsonCodec}

case class ClientInnerTicTacToe(override val fields: List[Field] = List.fill(9)(EMPTY), override val phase: Phase = TurnOfX)
  extends AbstractInnerTicTacToe(fields, phase) 

object ClientInnerTicTacToe {
  implicit val codec: JsonCodec[ClientInnerTicTacToe] = DeriveJsonCodec.gen[ClientInnerTicTacToe]
}
