package tictactoe.shared

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
  val row1 = List(0,1,2)
  val row2 = List(3,4,5)
  val row3 = List(6,7,8)

  val column1 = List(0,3,6)
  val column2 = List(1,4,7)
  val column3 = List(2,5,8)

  val diagonal1 = List(0,4,8)
  val diagonal2 = List(2,4,6)

}

