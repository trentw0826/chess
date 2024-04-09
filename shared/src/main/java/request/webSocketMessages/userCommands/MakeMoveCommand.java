package request.webSocketMessages.userCommands;

import chess.ChessMove;

/**
 * Used to request to make a move in a game.
 */
public class MakeMoveCommand {
  private final int gameID;
  private final ChessMove move;

  public MakeMoveCommand(int gameID, ChessMove move) {
    this.gameID = gameID;
    this.move = move;
  }
}
