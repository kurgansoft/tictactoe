package tictactoe.backend

import gbge.backend._
import gbge.shared.actions.{Action, GameAction}
import gbge.shared.{DecodeCapable, IN_PROGRESS, NOT_STARTED}
import tictactoe.shared._

case class TicTacToe(innerTicTacToe: Option[InnerTicTacToe] = None) extends AbstractTicTacToe(innerTicTacToe) with BackendGame[ClientTicTacToe] {

  override val noOfPlayers: Int = 2

  override def reduce(gameAction: GameAction, invoker: Option[Player]): (BackendGame[ClientTicTacToe], UniverseResult) =  {
    try {
      val action: TicTacToeAction = gameAction.asInstanceOf[TicTacToeAction]
      if (action.adminOnly && !invoker.exists(_.isAdmin))
        (this, UnauthorizedFailure("It is an admin-only action."))
      else
        this.reduce0(action, invoker.flatMap(_.role))
    } catch  {
      case _ : Throwable => {
        (this, GeneralFailure("The specified action is not supported in this game."))
      }
    }
  }

  def reduce0(action: TicTacToeAction, role: Option[Int]): (TicTacToe, UniverseResult) = action match {
    case Init => {
      if (state == NOT_STARTED) {
        (this.copy(innerTicTacToe = Some(InnerTicTacToe())), OK)
      } else {
        (this, GeneralFailure("Cannot init; game is started already."))
      }
    }
    case x: TicTacToeAction => {
      if (state == IN_PROGRESS) {
        val result: (InnerTicTacToe, UniverseResult) = innerTicTacToe.get.reduce(x, role)
        (this.copy(innerTicTacToe = Some(result._1)), result._2)
      } else {
        (this, GeneralFailure("Incorrect phase"))
      }
    }
  }

  override def toFrontendGame(role: Option[Int]): ClientTicTacToe = {
    ClientTicTacToe(innerTicTacToe.map(_.toClient))
  }
}

case class InnerTicTacToe(override val fields: List[Field] = List.fill(9)(EMPTY), override val phase: Phase = TurnOfX)
  extends AbstractInnerTicTacToe(fields, phase) {
  def reduce(action: TicTacToeAction, role: Option[Int]): (InnerTicTacToe, UniverseResult) = action match {
    case Restart => {
      (InnerTicTacToe(), OK)
    }
    case TakeMark(index) => {
      def phaseToRole(phase: Phase): Int = {
        phase match {
          case TurnOfX => 1
          case TurnOfO => 2
          case _ => 0
        }
      }

      if (!role.contains(phaseToRole(phase))) {
        (this, GeneralFailure("Not your turn."))
      } else {
        if (index < 0 || index >= 9) {
          (this, GeneralFailure())
        } else if (fields(index) != EMPTY) {
          (this, GeneralFailure())
        } else if (phase == GameOver) {
          (this, GeneralFailure())
        } else {
          val temp = this.copy(fields = fields.updated(index, phase.sign))
          if (temp.winner.isDefined || !temp.fields.contains(EMPTY))
            (temp.copy(phase = GameOver), OK)
          else
            (temp.copy(phase = phase.nextPhase), OK)
        }
      }
    }
    case _ => (this, OK)
  }

  val toClient: ClientInnerTicTacToe = {
    ClientInnerTicTacToe(fields, phase)
  }
}

object TicTacToe extends Startable {
  override def start(noOfPlayers: Int): (BackendGame[ClientTicTacToe], Option[Action]) = {
    (TicTacToe(), None)
  }

  override val frontendGame: DecodeCapable = ClientTicTacToe
  override val name: String = "TicTacToe"
}
