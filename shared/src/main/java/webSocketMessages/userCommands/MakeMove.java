package webSocketMessages.userCommands;

import chess.ChessMove;

/**
 * Used to request to make a move in a game.
 */
public class MakeMove {
  private final int gameID;
  private final ChessMove move;

  public MakeMove(int gameID, ChessMove move) {
    this.gameID = gameID;
    this.move = move;
  }
}
