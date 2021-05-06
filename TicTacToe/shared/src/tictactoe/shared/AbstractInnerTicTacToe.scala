package tictactoe.shared

import upickle.default.{macroRW, ReadWriter => RW}

abstract sealed class Phase {
  val sign: Field
  val role: Option[TicTacToeRole]
  val nextPhase: Phase
}
object TurnOfX extends Phase {
  override val sign: Field = X
  override val nextPhase: Phase = TurnOfO
  override val role: Option[TicTacToeRole] = Some(RoleX)
}
object TurnOfO extends Phase {
  override val sign: Field = O
  override val nextPhase: Phase = TurnOfX
  override val role: Option[TicTacToeRole] = Some(RoleO)
}
object GameOver extends Phase {
  override val sign: Field = EMPTY
  override val nextPhase: Phase = GameOver
  override val role: Option[TicTacToeRole] = None
}

object Phase {
  implicit def rw: RW[Phase] = macroRW
}

abstract sealed class Field {
  val getRole: Option[TicTacToeRole]
  val getRoleNumber: Option[Int]
}
object O extends Field {
  override def toString: String = "O"
  override val getRole: Option[TicTacToeRole] = Some(RoleO)
  override val getRoleNumber: Option[Int] = Some(2)
}
object X extends Field {
  override def toString: String = "X"
  override val getRole: Option[TicTacToeRole] = Some(RoleX)
  override val getRoleNumber: Option[Int] = Some(1)
}
object EMPTY extends Field {
  override def toString: String = ""
  override val getRole: Option[TicTacToeRole] = None
  override val getRoleNumber: Option[Int] = None
}

object Field {
  implicit def rw: RW[Field] = macroRW
}

abstract class AbstractInnerTicTacToe(val fields: List[Field] = List.fill(9)(EMPTY), val phase: Phase = TurnOfX) {

  def allTheSame(list: List[Int]): Option[Field] = {
    val t = list.map(fields(_))
    if (t.forall(_ == O))
      Some(O)
    else if (t.forall(_ == X))
      Some(X)
    else
      None
  }

  lazy val winners: List[Field] = List(
    allTheSame(AbstractInnerTicTacToe.row1),
    allTheSame(AbstractInnerTicTacToe.row2),
    allTheSame(AbstractInnerTicTacToe.row3),
    allTheSame(AbstractInnerTicTacToe.column1),
    allTheSame(AbstractInnerTicTacToe.column2),
    allTheSame(AbstractInnerTicTacToe.column3),
    allTheSame(AbstractInnerTicTacToe.diagonal1),
    allTheSame(AbstractInnerTicTacToe.diagonal2)
  ).flatten

  val winner: Option[Field] = winners.headOption
}

object AbstractInnerTicTacToe {
  implicit def rw: RW[TicTacToeAction] = macroRW

  val row1 = List(0,1,2)
  val row2 = List(3,4,5)
  val row3 = List(6,7,8)

  val column1 = List(0,3,6)
  val column2 = List(1,4,7)
  val column3 = List(2,5,8)

  val diagonal1 = List(0,4,8)
  val diagonal2 = List(2,4,6)

}

