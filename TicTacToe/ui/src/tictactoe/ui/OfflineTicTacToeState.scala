package tictactoe.ui

import gbge.client._
import gbge.shared.{FrontendPlayer, FrontendUniverse}
import gbge.ui.eps.player.ScreenEvent
import gbge.ui.state.OfflineState
import tictactoe.shared._
import zio.UIO

abstract sealed class OfflineTicTacToeStateAction extends ScreenEvent
case class FieldClicked(index: Int) extends OfflineTicTacToeStateAction
case object Restart extends OfflineTicTacToeStateAction
case object Init extends OfflineTicTacToeStateAction


case class OfflineTicTacToeState(cttt: ClientTicTacToe, yourRole: Option[TicTacToeRole]) extends OfflineState {
  override def handleScreenEvent(sa: ScreenEvent, fu: Option[FrontendUniverse], you: Option[FrontendPlayer]): (OfflineState, UIO[List[OfflineTicTacToeStateAction]]) = {
    sa match {
      case csa: OfflineTicTacToeStateAction => reduce0(csa, you.flatMap(_.token))
      case _ => this
    }
  }

  def reduce0(action: OfflineTicTacToeStateAction, token: Option[String]): (OfflineState, UIO[List[OfflineTicTacToeStateAction]]) = {
    action match {
      case FieldClicked(index) =>
        import tictactoe.shared._
        if (cttt.innerGame.map(_.fields(index)).contains(EMPTY) && yourRole.isDefined &&
            cttt.innerGame.flatMap(_.phase.role).contains(yourRole.get)) {
          (this, ClientEffects.submitRestActionWithToken(TakeMark(index), token))
        } else
          this
      case Restart =>
        (this, ClientEffects.submitRestActionWithToken(tictactoe.shared.Restart, token))
      case Init =>
        (this, ClientEffects.submitRestActionWithToken(tictactoe.shared.Init, token))
      case _ => this
    }
  }
}
