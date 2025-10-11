package tictactoe.backend.launchers

import gbge.backend.{BackendGameProps, GenericLauncher}
import tictactoe.backend.BackendTicTacToeProps
import zio.{Scope, ZIO, ZIOAppDefault}

object Launcher extends ZIOAppDefault {

    private val games: Seq[BackendGameProps[_, _]] = Seq(BackendTicTacToeProps)

    private val gl = GenericLauncher(games)

    override def run: ZIO[Scope, Any, Unit] = gl.launch

}
