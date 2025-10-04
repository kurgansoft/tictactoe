package tictactoe.shared

import gbge.shared.GameRole

sealed trait TicTacToeRole extends GameRole

object TicTacToeRole {
  def fromRoleId(roleId: Int): TicTacToeRole = roleId match {
    case 1 => RoleX
    case 2 => RoleO
  }
}

case object RoleX extends TicTacToeRole {
  override def toString: String = "X"
  override val roleId: Int = 1
}
case object RoleO extends TicTacToeRole {
  override def toString: String = "O"
  override val roleId: Int = 2
}
