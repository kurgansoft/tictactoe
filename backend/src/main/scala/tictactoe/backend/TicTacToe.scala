package tictactoe.backend

import gbge.backend.*
import gbge.backend.models.Player
import gbge.shared.GameState.{IN_PROGRESS, NOT_STARTED}
import gbge.shared.actions.{Action, GameAction}
import tictactoe.shared.*
import zio.{IO, ZIO}

case class TicTacToe(innerTicTacToe: Option[InnerTicTacToe] = None) extends AbstractTicTacToe(innerTicTacToe) with BackendGame[TicTacToeAction, ClientTicTacToe] {

  override val noOfPlayers: Int = 2

  override def reduce(gameAction: GameAction, invoker: Player): Either[Failure, (TicTacToe, IO[Nothing, Option[Action]])] =
    gameAction match {
      case ticTacToeAction: TicTacToeAction => reduce0(ticTacToeAction, invoker.role)
      case _ => Left(GeneralFailure("Provided action cannot be handled by this game."))
    }

  private def reduce0(action: TicTacToeAction, role: Option[Int]): Either[Failure, (TicTacToe, IO[Nothing, Option[Action]])] = action match {
    case Init =>
      if (state == NOT_STARTED) {
        Right((this.copy(innerTicTacToe = Some(InnerTicTacToe())), ZIO.none))
      } else {
        Left(GeneralFailure("Cannot init; game is started already."))
      }
    case ticTacToeAction: TicTacToeAction =>
      if (state == IN_PROGRESS) {
        innerTicTacToe.get.reduce(ticTacToeAction, role) match {
          case Left(failure) => Left(failure)
          case Right(innerTicTacToeTransformed) => Right((this.copy(innerTicTacToe = Some(innerTicTacToeTransformed)), ZIO.none)) 
        }
      } else {
        Left(GeneralFailure("Incorrect phase"))
      }
  }

  override def toFrontendGame(role: Option[Int]): ClientTicTacToe = {
    ClientTicTacToe(innerTicTacToe.map(_.toClient))
  }
}
