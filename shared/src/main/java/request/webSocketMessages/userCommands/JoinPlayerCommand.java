package request.webSocketMessages.userCommands;

import playerColor.PlayerColor;

/**
 * Used for a user to request to join a game.
 */
public class JoinPlayerCommand extends UserGameCommand {
  private final int gameID;
  private final PlayerColor playerColor;

  public JoinPlayerCommand(int gameID, String authToken, PlayerColor playerColor) {
    super(authToken);
    this.gameID = gameID;
    this.playerColor = playerColor;
  }

  public int getGameID() {
    return gameID;
  }

  public PlayerColor getPlayerColor() {
    return playerColor;
  }
}
