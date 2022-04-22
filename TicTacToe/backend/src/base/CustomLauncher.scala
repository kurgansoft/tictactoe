package base

import gbge.backend.server.CustomServer
import tictactoe.backend.TicTacToe
import tictactoe.shared.ClientTicTacToe

object CustomLauncher extends CustomServer {
  gbge.shared.RG.registeredGames = List(ClientTicTacToe)
  gbge.backend.RG.registeredGames = List(TicTacToe)

  assert(gbge.backend.RG.registeredGames.size == gbge.shared.RG.registeredGames.size)
  gbge.backend.RG.registeredGames.zip(gbge.shared.RG.registeredGames).foreach(a => {
    assert(a._1.frontendGame == a._2)
  })

  override val jsLocation: Option[String] = Some("out/TicTacToe/ui/fastOpt.dest")
}
