package request.webSocketMessages.userCommands;

/**
 * Tells the server you are leaving the game so it will stop sending you notifications.
 */
public class LeaveCommand extends UserGameCommand {
  private final int gameID;

  public LeaveCommand(String authToken, int gameID) {
    super(authToken);
    this.gameID = gameID;
  }
}
