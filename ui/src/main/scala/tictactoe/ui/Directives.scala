package tictactoe.ui

import gbge.shared.{FrontendGame, FrontendPlayer}
import gbge.shared.GameState.NOT_STARTED
import gbge.ui.eps.player.ClientState
import japgolly.scalajs.react.Callback
import japgolly.scalajs.react.vdom.TagOf
import japgolly.scalajs.react.vdom.all.{div, *}
import org.scalajs.dom.html.Div
import tictactoe.shared.*
import tictactoe.ui
import uiglue.{Event, EventLoop}

object Directives {

  def text(role: Option[TicTacToeRole], phase: Phase, winner: Option[TicTacToeRole])(players: List[FrontendPlayer]): String = {
    phase match {
      case TurnOfX =>
        if (role.contains(RoleX)) "This is your turn, pick your mark."
        else s"Waiting for X (${getPlayerNameForRole(players, 1)}) to pick a mark."
      case TurnOfO =>
        if (role.contains(RoleO)) "This is your turn, pick your mark."
        else s"Waiting for O (${getPlayerNameForRole(players, 2)}) to pick a mark."
      case GameOver =>
        if (winner.isEmpty) "Game is over. It is a draw."
        else {
          if (role.isDefined) {
            if (role.contains(winner.get)) {
              "Game is over. You have won!"
            } else {
              "Game is over. You have lost :-("
            }
          } else
            s"Game is over. Winner is ${winner.get.toString} (${getPlayerNameForRole(players, winner.get.roleId)})"
        }
    }
  }

  def getPlayerNameForRole(players: List[FrontendPlayer], role: Int): String = {
    players.find(_.role.contains(role)).map(_.name).getOrElse(FrontendPlayer.MISSING_PLAYERS_NAME)
  }

  def player(clientState: ClientState, eventHandler: EventLoop.EventHandler[Event]): TagOf[Div] = {
    val tttGame = clientState.frontendUniverse.get.game.get.asInstanceOf[ClientTicTacToe]
    val players = clientState.frontendUniverse.get.players
    val you = clientState.player
    val yourRole: Option[TicTacToeRole] = you.flatMap(_.role).flatMap(tttGame.getRoleById)
    if (tttGame.state == NOT_STARTED) {
      div(
        gbge.ui.display.GeneralDirectives.generalRoleChooserScreen(you.get, players, tttGame, eventHandler),
        Option.when(you.exists(_.isAdmin))
        (div(display:="flex", flexDirection:="column", alignItems:="center",
          button(`class`:="btn btn-primary", "Launch game", onClick --> Callback {
            eventHandler(ui.Init)
          })
        ))
      )
    } else {
      val board = tttGame.innerGame.get.fields

      div(display:="flex", flexDirection:="column", alignItems:="center", backgroundColor:="white", height:="100%",
        div(text(yourRole, tttGame.innerGame.get.phase, tttGame.innerGame.get.winner.flatMap(_.getRole))(players), height:="200px", verticalAlign:="center"),
        div(width:="50%",
          displayBoard(board, Some(eventHandler))
        ),br,br,br,
        Option.when(you.exists(_.isAdmin))
        (button(`class`:="btn btn-primary", "RESTART", onClick --> Callback {
          eventHandler(ui.Restart)
        })),
        div(position:="absolute", bottom:="15px", right:="15px",
          s"You are: ${you.get.name}",br,
          if (yourRole.isDefined) s"Your role: ${yourRole.get}"
          else "You are not part of this game."
        )
      )

    }
  }

  def displayBoard(fields: List[Field], optionalEventHandler: Option[EventLoop.EventHandler[FieldClicked]] = None): TagOf[Div] = {
    div(width:="100%", display:= "grid", gridTemplateColumns:="33.3% 33.3% 33.3%",
      (for(field <- fields.zipWithIndex)
        yield fieldDisplayer(field._1, field._2, optionalEventHandler)).toTagMod
    )
  }

  def fieldDisplayer(field: Field, index: Int, optionalEventHandler: Option[EventLoop.EventHandler[FieldClicked]] = None): TagOf[Div] = {
    val s2 = borderBottom := "solid 15px brown"
    val s3 = borderRight := "solid 15px brown"
    val style =
      List(display:= "flex", flexDirection:= "column", justifyContent:= "center") ++
      List(paddingBottom:= "100%", width:= "100%", position:= "relative") ++
      List(backgroundColor:="white", borderRadius:= "15px") ++
      Option.when(index < 6)(s2) ++
      Option.when(index%3 != 2)(s3)
    div(
      div(position:= "absolute", top:= "0", bottom:= "0", left:= "0", right:= "0",
        div(field.toString)
      )(fontSize:="xxx-large", textAlign:="center", display:= "flex", flexDirection:= "column", justifyContent:="center")
    )(onClick --> Callback {
      optionalEventHandler.map(_(FieldClicked(index)))
    })(style.toTagMod)
  }

  def spectator(frontendGame: FrontendGame[_], players: List[FrontendPlayer]): TagOf[Div] = {
    val ticTacToe = frontendGame.asInstanceOf[ClientTicTacToe]
    if (ticTacToe.state == NOT_STARTED) {
      div(position:="absolute", top:="0px", bottom:="0px", left:="0px", right:="0px",
        roleChoosingScreen(ticTacToe)(players)
      )
    } else {
      val innerTicTacToe = ticTacToe.innerGame.get
      div(position:="absolute", top:="0px", bottom:="0px", left:="0px", right:="0px",
        div(display:="flex", flexDirection:="column", alignItems:="center", backgroundColor:="white", height:="100%", justifyContent:= "space-evenly",
          div(text(None, innerTicTacToe.phase, innerTicTacToe.winner.flatMap(_.getRole))(players)),
          displayBoard(ticTacToe.innerGame.get.fields)(width:="50%"),
        ),
        div(
          Option.when(players.exists(_.role.isEmpty))
          (div(
            div("People with no roles:"),
            (for (p <- players.filter(_.role.isEmpty))
              yield div(p.name)).toTagMod
          )), hr,
          div("X: " + players.find(_.role.contains(1)).map(_.name).getOrElse(FrontendPlayer.MISSING_PLAYERS_NAME)),
          div("O: " + players.find(_.role.contains(2)).map(_.name).getOrElse(FrontendPlayer.MISSING_PLAYERS_NAME))
        )(position:="fixed", right:="10px", bottom:="10px", width:="250px")
      )
    }
  }

  def roleChoosingScreen(ttt: ClientTicTacToe)(players: List[FrontendPlayer]): TagOf[Div] = {
    val playerX = players.find(_.role.contains(1)).map(_.name).map(div(_)).getOrElse(div(i("This role is not taken just yet.")))
    val player0 = players.find(_.role.contains(2)).map(_.name).map(div(_)).getOrElse(div(i("This role is not taken just yet.")))

    val headerStyle =
      List(
        fontSize:= "2.5vw",
        backgroundColor:= "olivedrab",
        textAlign:= "center",
        height:= "10%",
        display:="flex",
        flexDirection:="column",
        justifyContent:="space-evenly"
      ).toTagMod

    val styleLeft = List(
      padding:= "0px",
      display:= "flex",
      flexDirection:="column",
      justifyContent:= "space-evenly",
      alignItems:= "stretch",
      width:= "100%",
      height:= "100%",
      textAlign:= "center"
    ).toTagMod

    val styleRight = List(
      width:= "100%",
      backgroundColor:= "#656565",
      position:="relative"
    ).toTagMod

    val contentStyle = List(
      display:= "flex",
      flexDirection:= "row",
      justifyContent:= "space-evenly",
      fontSize:= "2.5vw",
      height:= "90%"
    ).toTagMod

    div(backgroundColor:="green", position:="absolute", top:="0px", left:="0px", right:="0px", bottom:="0px", border:= "5px solid black",
      div(
        div("TicTacToe")
      )(headerStyle),
      div(
        div(
          div(br, div(u("Role X")), br, playerX)(backgroundColor:= "#4c674a"),
          div(br, div(u("Role O")), br, player0)(backgroundColor:= "#65b95f")
        )(styleLeft),
        div(
          div(br, "Players with no roles:",br,br, height:= "100%", position:= "relative",
            div(position:="relative", left:="10px",
              ul(
                (for (player <- players.filter(_.role.isEmpty))
                  yield li(player.name)).toTagMod
              )
            )
          ),
          div("Admin: " + players.find(_.isAdmin).map(_.name).getOrElse("...no admin..."))
          (position:= "absolute", bottom:= "5px", right:= "5px")
        )(styleRight)
      )(contentStyle)
    )
  }

  def adminActions(clientState: ClientState, eventHandler : EventLoop.EventHandler[Event]): TagOf[Div] = {
    div()
  }
}
