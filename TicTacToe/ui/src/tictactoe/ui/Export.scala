package tictactoe.ui

import uiglue.{Event, EventLoop}
import zio.UIO
import gbge.shared.FrontendUniverse
import gbge.ui.UIExport
import gbge.ui.eps.player.ClientState
import gbge.ui.eps.spectator.SpectatorState
import japgolly.scalajs.react.vdom.TagOf
import org.scalajs.dom.html.Div
import tictactoe.shared.ClientTicTacToe

object Export extends UIExport {
  override val playerDisplayer: (ClientState, EventLoop.EventHandler[Event]) => TagOf[Div] = Directives.player
  override val spectatorDisplayer: (SpectatorState, EventLoop.EventHandler[Event]) => TagOf[Div] = (state, _ ) =>{
    val fu = state.frontendUniverse.get
    val game = fu.game.get
    Directives.spectator(game, fu.players)
  }
  override val handleNewFU: (ClientState, FrontendUniverse) => (ClientState, EventLoop.EventHandler[Event] => UIO[List[Event]]) = (clientState, fu) => {
    val cttt = fu.game.get.asInstanceOf[ClientTicTacToe]
    val yr = clientState.you.flatMap(_.role.flatMap(cttt.getRoleById))
    (clientState.copy(frontendUniverse = Some(fu), offlineState = OfflineTicTacToeState(cttt, yr)), _ => UIO.succeed(List.empty))
  }
  override val adminDisplayer: (ClientState, EventLoop.EventHandler[Event]) => TagOf[Div] = Directives.adminActions
}