package webSocketMessages.userCommands;

//TODO Make player color some shared enum rather than a string

/**
 * Used for a user to request to join a game.
 */
public class JoinPlayer extends UserGameCommand {
  private final int gameID;
  private final String playerColor;

  public JoinPlayer(String authToken, int gameID, String playerColor) {
    super(authToken);
    this.gameID = gameID;
    this.playerColor = playerColor;
  }
}
