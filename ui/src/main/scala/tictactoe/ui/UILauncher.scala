package tictactoe.ui

import gbge.shared.FrontendGame
import gbge.shared.actions.GameAction
import gbge.ui.EntryPoint
import tictactoe.shared.ClientTicTacToe
import zio.blocks.schema.json.JsonCodec

import scala.scalajs.js.annotation.JSExportTopLevel

@JSExportTopLevel("ep")
object UILauncher extends EntryPoint {
  gbge.shared.RG.gameCodecs = List(
    ClientTicTacToe.jsonCodec
      .asInstanceOf[JsonCodec[FrontendGame[? <: GameAction]]]
  )
  gbge.ui.RG.registeredGames = List(ClientTicTacToeProps)
}
