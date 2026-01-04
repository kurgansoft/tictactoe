package tictactoe.ui

import gbge.client.{DispatchActionWithToken, GeneralEvent}
import gbge.shared.FrontendUniverse
import gbge.ui.eps.player.ScreenEvent
import gbge.ui.state.OfflineState
import tictactoe.shared.*
import zio.{UIO, ZIO}

abstract sealed class OfflineTicTacToeStateAction extends ScreenEvent
case class FieldClicked(index: Int) extends OfflineTicTacToeStateAction
case object Restart extends OfflineTicTacToeStateAction
case object Init extends OfflineTicTacToeStateAction

case class OfflineTicTacToeState(cttt: ClientTicTacToe, yourRole: Option[TicTacToeRole]) extends OfflineState[Any] {

  override def handleScreenEvent(sa: ScreenEvent, fu: Option[FrontendUniverse], playerId: Option[Int]): (OfflineState[Any], UIO[List[GeneralEvent]]) = {
    sa match {
      case csa: OfflineTicTacToeStateAction => reduce0(csa)
      case _ => (this, ZIO.succeed(List.empty))
    }
  }

  private def reduce0(action: OfflineTicTacToeStateAction): (OfflineState[Any], UIO[List[GeneralEvent]]) = {
    action match {
      case FieldClicked(index) =>
        import tictactoe.shared.*
        if (cttt.innerGame.map(_.fields(index)).contains(EMPTY) && yourRole.isDefined &&
            cttt.innerGame.flatMap(_.phase.role).contains(yourRole.get)) {
          (this, ZIO.succeed(List(DispatchActionWithToken(tictactoe.shared.TakeMark(index)))))
        } else
          (this, ZIO.succeed(List.empty))
      case Restart =>
        (this, ZIO.succeed(List(DispatchActionWithToken(tictactoe.shared.Restart))))
      case Init =>
        (this, ZIO.succeed(List(DispatchActionWithToken(tictactoe.shared.Init))))
    }
  }
}
