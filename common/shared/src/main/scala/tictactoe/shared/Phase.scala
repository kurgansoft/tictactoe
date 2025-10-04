package tictactoe.shared

import zio.json.{DeriveJsonCodec, JsonCodec}

sealed trait Phase {
  val sign: Field
  val role: Option[TicTacToeRole]
  val nextPhase: Phase
}
case object TurnOfX extends Phase {
  override val sign: Field = X
  override val nextPhase: Phase = TurnOfO
  override val role: Option[TicTacToeRole] = Some(RoleX)
}
case object TurnOfO extends Phase {
  override val sign: Field = O
  override val nextPhase: Phase = TurnOfX
  override val role: Option[TicTacToeRole] = Some(RoleO)
}
case object GameOver extends Phase {
  override val sign: Field = EMPTY
  override val nextPhase: Phase = GameOver
  override val role: Option[TicTacToeRole] = None
}

object Phase {
  implicit val codec: JsonCodec[Phase] = DeriveJsonCodec.gen[Phase]
}
