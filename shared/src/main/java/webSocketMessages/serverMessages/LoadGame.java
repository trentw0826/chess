package webSocketMessages.serverMessages;

import model.GameData;

/**
 * Used by the server to send the current game state to a client.
 * When a client receives this message, it will redraw the chess board.
 */
public class LoadGame extends ServerMessage {
  private final GameData game;

  public LoadGame(ServerMessageType type, GameData game) {
    super(type);
    this.game = game;
  }
}
