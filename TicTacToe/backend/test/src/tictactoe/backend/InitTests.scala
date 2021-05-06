package tictactoe.backend

import gbge.backend.{Failure, OK, Player}
import gbge.shared.IN_PROGRESS
import org.scalatest.funsuite.AnyFunSuite
import tictactoe.shared._

class InitTests extends AnyFunSuite {
  test("The admin can init the game.") {
    val theAdminPlayer = Player(1, "theAdmin", "", isAdmin = true)
    val ttt = TicTacToe()
    val result = ttt.reduce(Init, Some(theAdminPlayer))

    assert(result._1.state == IN_PROGRESS)
    assert(result._2 == OK)
  }

  test("Non admin cannot init the game.") {
    val theNonAdminPlayer = Player(1, "nonAdmin", "")
    val ttt = TicTacToe()
    val result = ttt.reduce(Init, Some(theNonAdminPlayer))

    assert(result._1 == ttt)
    assert(result._2.isInstanceOf[Failure])
  }

  test("Game cannot be initiated with no invoker.") {
    val ttt = TicTacToe()
    val result = ttt.reduce(Init, None)

    assert(result._1 == ttt)
    assert(result._2.isInstanceOf[Failure])
  }

}
