package tictactoe.ui

import gbge.shared.FrontendUniverse
import gbge.ui.ClientGameProps
import gbge.ui.eps.player.ClientState
import gbge.ui.eps.spectator.SpectatorState
import japgolly.scalajs.react.vdom.TagOf
import org.scalajs.dom.html.Div
import tictactoe.shared.{ClientTicTacToe, TicTacToeAction, TicTacToeProps, TicTacToeRole}
import uiglue.Event
import uiglue.EventLoop.EventHandler
import zio.{UIO, ZIO}

object ClientTicTacToeProps extends TicTacToeProps with ClientGameProps[TicTacToeAction, ClientTicTacToe] {
  override val playerDisplayer: (ClientState, EventHandler[Event]) => TagOf[Div] =
    Directives.player
    
  override val spectatorDisplayer: (SpectatorState, EventHandler[Event]) => TagOf[Div] = (state, _ ) => {
    val fu = state.frontendUniverse.get
    val game = fu.game.get
    Directives.spectator(game, fu.players)
  }

  override val handleNewFU: (ClientState, FrontendUniverse) => (ClientState, EventHandler[Event] => UIO[List[Event]]) =
    (clientState, fu) => {
      val cttt = fu.game.get.asInstanceOf[ClientTicTacToe]
      val yr = clientState.you.map(pair => {
        val roleId = pair._1
        TicTacToeRole.fromRoleId(roleId)
      })
      (clientState.copy(frontendUniverse = Some(fu), offlineState = OfflineTicTacToeState(cttt, yr)), _ => ZIO.succeed(List.empty))
    }
    
  override val adminDisplayer: (ClientState, EventHandler[Event]) => TagOf[Div] =
    Directives.adminActions
}
