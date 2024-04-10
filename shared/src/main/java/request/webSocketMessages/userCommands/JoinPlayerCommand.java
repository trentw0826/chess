package request.webSocketMessages.userCommands;

import playerColor.PlayerColor;

/**
 * Used for a user to request to join a game.
 */
public class JoinPlayerCommand extends UserGameCommand {
  private final int gameID;
  private final PlayerColor playerColor;

  public JoinPlayerCommand(String authToken, int gameID, PlayerColor playerColor) {
    super(authToken);
    this.commandType = CommandType.JOIN_PLAYER;
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
