package tictactoe.backend

import gbge.backend.{Failure, OK, Player}
import org.scalatest.funsuite.AnyFunSuite
import tictactoe.shared._

class TakeMarkTests extends AnyFunSuite {
  test("Cannot take mark if game is not in progress") {
    val player1 = Player(1, "Player1", "",role = Some(1))
    val ttt = TicTacToe()
    val result = ttt.reduce(TakeMark(1), Some(player1))

    assert(result._1 == ttt)
    assert(result._2.isInstanceOf[Failure])
  }

  test("Cannot take mark if it is not your turn.") {
    val player1 = Player(1, "Player1", "",role = Some(2))
    val ttt = TicTacToe(Some(InnerTicTacToe()))
    val result = ttt.reduce(TakeMark(1), Some(player1))

    assert(result._1 == ttt)
    assert(result._2.isInstanceOf[Failure])
  }

  test("Success") {
    val player1 = Player(1, "Player1", "",role = Some(1))
    val ttt = TicTacToe(Some(InnerTicTacToe()))
    val result = ttt.reduce(TakeMark(1), Some(player1))

    assert(result._2 == OK)
    assert(result._1.isInstanceOf[TicTacToe])
    val newTicTacToeGame = result._1.asInstanceOf[TicTacToe]

    assert(newTicTacToeGame.innerTicTacToe.get.fields(1) == X)
  }
}
