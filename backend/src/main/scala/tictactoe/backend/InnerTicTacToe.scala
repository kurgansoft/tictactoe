package tictactoe.backend

import gbge.backend.GeneralFailure
import tictactoe.shared.*

case class InnerTicTacToe(override val fields: List[Field] = List.fill(9)(EMPTY), override val phase: Phase = TurnOfX)
  extends AbstractInnerTicTacToe(fields, phase) {
  def reduce(action: TicTacToeAction, role: Option[Int]): Either[GeneralFailure, InnerTicTacToe] = action match {
    case Restart =>
      Right(InnerTicTacToe())
    case TakeMark(index) =>
      def phaseToRole(phase: Phase): Int = {
        phase match {
          case TurnOfX => 1
          case TurnOfO => 2
          case _ => 0
        }
      }

      if (!role.contains(phaseToRole(phase))) {
        Left(GeneralFailure("Not your turn."))
      } else {
        if (index < 0 || index >= 9) {
          Left(GeneralFailure())
        } else if (fields(index) != EMPTY) {
          Left(GeneralFailure())
        } else if (phase == GameOver) {
          Left(GeneralFailure())
        } else {
          val temp = this.copy(fields = fields.updated(index, phase.sign))
          if (temp.winner.isDefined || !temp.fields.contains(EMPTY))
            Right(temp.copy(phase = GameOver))
          else
            Right(temp.copy(phase = phase.nextPhase))
        }
      }
    case _ => Right(this)
  }

  val toClient: ClientInnerTicTacToe = {
    ClientInnerTicTacToe(fields, phase)
  }
}