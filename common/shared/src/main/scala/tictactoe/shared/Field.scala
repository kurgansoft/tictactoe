package tictactoe.shared

sealed trait Field {
  val getRole: Option[TicTacToeRole]
  val getRoleNumber: Option[Int]
}

case object O extends Field {
  override def toString: String = "O"
  override val getRole: Option[TicTacToeRole] = Some(RoleO)
  override val getRoleNumber: Option[Int] = Some(2)
}
case object X extends Field {
  override def toString: String = "X"
  override val getRole: Option[TicTacToeRole] = Some(RoleX)
  override val getRoleNumber: Option[Int] = Some(1)
}
case object EMPTY extends Field {
  override def toString: String = ""
  override val getRole: Option[TicTacToeRole] = None
  override val getRoleNumber: Option[Int] = None
}

