package tictactoe.backend

import tictactoe.shared.{ClientTicTacToe, TicTacToeAction, TicTacToeProps}
import gbge.backend.{BackendGame, BackendGameProps}
import gbge.shared.actions.Action

object BackendTicTacToeProps extends TicTacToeProps with BackendGameProps[TicTacToeAction, ClientTicTacToe] {
  override def start(noOfPlayers: Int): (BackendGame[TicTacToeAction, ClientTicTacToe], Option[Action]) = {
    (TicTacToe(), None)
  }
}
