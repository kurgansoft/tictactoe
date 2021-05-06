package tictactoe.ui

import gbge.client._
import gbge.shared.{FrontendPlayer, FrontendUniverse}
import gbge.ui.eps.player.ScreenEvent
import gbge.ui.state.OfflineState
import tictactoe.shared._

abstract sealed class OfflineTicTacToeStateAction extends ScreenEvent
case class FieldClicked(index: Int) extends OfflineTicTacToeStateAction
case object Restart extends OfflineTicTacToeStateAction
case object Init extends OfflineTicTacToeStateAction


case class OfflineTicTacToeState(cttt: ClientTicTacToe, yourRole: Option[TicTacToeRole]) extends OfflineState {
  override def handleScreenEvent(sa: ScreenEvent, fu: Option[FrontendUniverse], you: Option[FrontendPlayer]): (OfflineState, ClientResult) = {
    sa match {
      case csa: OfflineTicTacToeStateAction => reduce0(csa)
      case _ => this
    }
  }

  def reduce0(action: OfflineTicTacToeStateAction): (OfflineState, ClientResult) = {
    action match {
      case FieldClicked(index) => {
        import tictactoe.shared._
        if (cttt.innerGame.map(_.fields(index)).contains(EMPTY) && yourRole.isDefined &&
            cttt.innerGame.flatMap(_.phase.role).contains(yourRole.get)) {
          (this, PrepareRestActionWithToken(TakeMark(index)))
        } else
          (this, OK)
      }
      case Restart => {
        (this, PrepareRestActionWithToken(tictactoe.shared.Restart))
      }
      case Init => {
        (this, PrepareRestActionWithToken(tictactoe.shared.Init))
      }
      case _ => (this, OK)
    }
  }
}
