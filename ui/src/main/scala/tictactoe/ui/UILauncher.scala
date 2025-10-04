package tictactoe.ui

import gbge.shared.FrontendGame
import gbge.shared.actions.GameAction
import gbge.ui.EntryPoint
import tictactoe.shared.ClientTicTacToe
import zio.json.JsonCodec

import scala.scalajs.js.annotation.JSExportTopLevel

@JSExportTopLevel("ep")
object UILauncher extends EntryPoint {
  gbge.shared.RG.gameCodecs = List(ClientTicTacToe.codec.asInstanceOf[JsonCodec[FrontendGame[_ <: GameAction]]])
  gbge.ui.RG.registeredGames = List(ClientTicTacToeProps)
}
