package webSocketMessages.userCommands;

/**
 * Used to request to start observing a game.
 */
public class JoinObserver extends UserGameCommand {
  private final int gameID;

  public JoinObserver(String authToken, int gameID) {
    super(authToken);
    this.gameID = gameID;
  }
}
