package tictactoe.backend

import tictactoe.shared.{ClientTicTacToe, TicTacToeAction, TicTacToeProps}
import gbge.backend.{BackendGame, BackendGameProps}
import gbge.shared.actions.Action
import zio.http.codec.ContentCodec
import zio.http.codec.HttpCodec.content

object BackendTicTacToeProps extends TicTacToeProps with BackendGameProps[TicTacToeAction, ClientTicTacToe] {
  override def start(noOfPlayers: Int): (BackendGame[TicTacToeAction, ClientTicTacToe], Option[Action]) = {
    (TicTacToe(), None)
  }

  override val contentCodec: ContentCodec[TicTacToeAction] = content[TicTacToeAction]
}
