package tictactoe.shared

import upickle.default.{macroRW, ReadWriter => RW}

case class ClientInnerTicTacToe(override val fields: List[Field] = List.fill(9)(EMPTY), override val phase: Phase = TurnOfX)
  extends AbstractInnerTicTacToe(fields, phase)

object ClientInnerTicTacToe {
  implicit def rw: RW[ClientInnerTicTacToe] = macroRW
}
