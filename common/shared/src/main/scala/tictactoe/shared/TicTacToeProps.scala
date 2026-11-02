package tictactoe.shared

import gbge.shared.GameProps
import zio.blocks.schema.json.JsonCodec

trait TicTacToeProps extends GameProps[TicTacToeAction, ClientTicTacToe] {
  override val name: String = "TicTacToe"
  override val urlFragment: String = "tictactoe"

  override val actionCodec: JsonCodec[TicTacToeAction] = TicTacToeAction.jsonCodec
  override val gameCodec: JsonCodec[ClientTicTacToe] = ClientTicTacToe.jsonCodec

}
