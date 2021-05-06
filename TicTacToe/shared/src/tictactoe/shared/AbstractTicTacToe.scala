package tictactoe.shared

import gbge.shared._

abstract sealed class TicTacToeRole extends GameRole

object RoleX extends TicTacToeRole {
  override def toString: String = "X"
  override val roleId: Int = 1
}
object RoleO extends TicTacToeRole {
  override def toString: String = "O"
  override val roleId: Int = 2
}

abstract class AbstractTicTacToe(innerGame: Option[AbstractInnerTicTacToe] = None) extends Game {

  val state: GameState = if (innerGame.isDefined) IN_PROGRESS else NOT_STARTED

  override val minPlayerNumber: Int = 2
  override val maxPlayerNumber: Int = 2
  override val roles: List[TicTacToeRole] = List(RoleX, RoleO)

  override def getRoleById(id: Int): Option[TicTacToeRole] = super.getRoleById(id).map(_.asInstanceOf[TicTacToeRole])
}

