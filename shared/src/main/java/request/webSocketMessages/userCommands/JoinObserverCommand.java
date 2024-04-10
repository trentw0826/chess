package request.webSocketMessages.userCommands;

/**
 * Used to request to start observing a game.
 */
public class JoinObserverCommand extends UserGameCommand {
  private final int gameID;

  public JoinObserverCommand(String authToken, int gameID) {
    super(authToken);
    this.commandType = CommandType.JOIN_OBSERVER;
    this.gameID = gameID;
  }
}
