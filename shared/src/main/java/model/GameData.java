package model;

import chess.ChessGame;

/**
 * Holds data representing a chess game on the server.
 *
 * @param gameID        the game's ID
 * @param whiteUsername the white player's username
 * @param blackUsername the black player's username
 * @param gameName      the game's name
 * @param game          the game object representing the game itself
 */
public record GameData(Integer gameID, String whiteUsername, String blackUsername,
                       String gameName, ChessGame game) implements DataModel {
  @Override
  public boolean hasNullFields() {
    return (gameID == null || whiteUsername == null ||
            blackUsername == null || gameName == null || game == null);
  }
}
