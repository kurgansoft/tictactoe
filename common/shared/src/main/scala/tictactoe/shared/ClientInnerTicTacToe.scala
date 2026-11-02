package tictactoe.shared

case class ClientInnerTicTacToe(override val fields: List[Field] = List.fill(9)(EMPTY), override val phase: Phase = TurnOfX)
  extends AbstractInnerTicTacToe(fields, phase) 
