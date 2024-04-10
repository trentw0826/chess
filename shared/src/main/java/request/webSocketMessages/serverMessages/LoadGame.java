package request.webSocketMessages.serverMessages;

import chess.ChessGame;

import java.util.Objects;

/**
 * Used by the server to send the current game state to a client.
 * When a client receives this message, it will redraw the chess board.
 */
public class LoadGame extends ServerMessage {
  private final ChessGame game;

  public LoadGame(ChessGame game) {
    super(ServerMessageType.LOAD_GAME);
    this.game = game;
  }

  public ChessGame getGame() {
    return game;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    LoadGame loadGame = (LoadGame) o;
    return Objects.equals(game, loadGame.game);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), game);
  }
}
