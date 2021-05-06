package tictactoe.ui

import gbge.client._
import gbge.shared.FrontendUniverse
import gbge.ui.UIExport
import gbge.ui.eps.player.ClientState
import gbge.ui.eps.spectator.SpectatorState
import japgolly.scalajs.react.vdom.TagOf
import org.scalajs.dom.html.Div
import tictactoe.shared.ClientTicTacToe

object Export extends UIExport {
  override val playerDisplayer: (ClientState, ClientEventHandler[ClientEvent]) => TagOf[Div] = Directives.player
  override val spectatorDisplayer: (SpectatorState, ClientEventHandler[ClientEvent]) => TagOf[Div] = (state, _ ) =>{
    val fu = state.frontendUniverse.get
    val game = fu.game.get
    Directives.spectator(game, fu.players)
  }
  override val handleNewFU: (ClientState, FrontendUniverse) => (ClientState, ClientResult) = (clientState, fu) => {
    val cttt = fu.game.get.asInstanceOf[ClientTicTacToe]
    val yr = clientState.you.flatMap(_.role.flatMap(cttt.getRoleById))
    (clientState.copy(frontendUniverse = Some(fu), offlineState = OfflineTicTacToeState(cttt, yr)), OK)
  }
  override val adminDisplayer: (ClientState, ClientEventHandler[ClientEvent]) => TagOf[Div] = Directives.adminActions
}