package tictactoe.backend

import gbge.backend.{Failure, OK, Player}
import gbge.shared.IN_PROGRESS
import org.scalatest.funsuite.AnyFunSuite
import tictactoe.shared._

class RestartTests extends AnyFunSuite {
  test("The admin can restart the game, if it is in progress") {
    val theAdminPlayer = Player(1, "theAdmin", "", isAdmin = true)
    val ttt = TicTacToe(innerTicTacToe = Some(InnerTicTacToe()))
    val result = ttt.reduce(Restart, Some(theAdminPlayer))

    assert(result._1.isInstanceOf[TicTacToe])
    val cttt = result._1.asInstanceOf[TicTacToe]

    assert(cttt.state == IN_PROGRESS)
    assert(cttt.innerTicTacToe.contains(InnerTicTacToe()))
    assert(result._2 == OK)
  }

  test("After restart by admin all the fields are empty.") {
    val theAdminPlayer = Player(1, "theAdmin", "", isAdmin = true)
    val ttt = TicTacToe(innerTicTacToe =
      Some(InnerTicTacToe(fields = List.fill(9)(EMPTY).updated(1, X), phase = TurnOfO)))
    val result = ttt.reduce(Restart, Some(theAdminPlayer))

    assert(result._1.isInstanceOf[TicTacToe])
    val cttt = result._1.asInstanceOf[TicTacToe]

    assert(cttt.state == IN_PROGRESS)
    assert(cttt.innerTicTacToe.contains(InnerTicTacToe()))
    assert(result._2 == OK)
  }

  test("Game cannot be restarted if it is not in progress.") {
    val theAdminPlayer = Player(1, "theAdmin", "", isAdmin = true)
    val ttt = TicTacToe()
    val result = ttt.reduce(Restart, Some(theAdminPlayer))
    assert(result._1 == ttt)
    assert(result._2.isInstanceOf[Failure])
  }

  test("Non admin cannot restart the game") {
    val theNonAdminPlayer = Player(1, "theNonAdmin", "")
    val ttt = TicTacToe(innerTicTacToe = Some(InnerTicTacToe()))
    val result = ttt.reduce(Restart, Some(theNonAdminPlayer))

    assert(result._1 == ttt)
    assert(result._2.isInstanceOf[Failure])
  }

}
