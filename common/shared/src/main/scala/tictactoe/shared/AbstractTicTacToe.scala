package tictactoe.shared

import gbge.shared.*
import gbge.shared.GameState.{IN_PROGRESS, NOT_STARTED}

abstract class AbstractTicTacToe(innerGame: Option[AbstractInnerTicTacToe] = None) extends Game {

  val state: GameState = if (innerGame.isDefined) IN_PROGRESS else NOT_STARTED

  override val minPlayerNumber: Int = 2
  override val maxPlayerNumber: Int = 2
  override val roles: List[TicTacToeRole] = List(RoleX, RoleO)

  override def getRoleById(id: Int): Option[TicTacToeRole] = super.getRoleById(id).map(_.asInstanceOf[TicTacToeRole])
}

