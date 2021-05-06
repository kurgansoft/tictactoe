package tictactoe.base

import gbge.ui.EntryPoint
import tictactoe.shared.ClientTicTacToe
import tictactoe.ui.Export

import scala.scalajs.js.annotation.JSExportTopLevel

@JSExportTopLevel("ep")
object UILauncher extends EntryPoint {
  gbge.shared.RG.registeredGames = List(ClientTicTacToe)
  gbge.ui.RG.registeredGames = List(Export)

  assert(gbge.ui.RG.registeredGames.size == gbge.shared.RG.registeredGames.size)
}
