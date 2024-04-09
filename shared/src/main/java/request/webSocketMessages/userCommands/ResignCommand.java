package request.webSocketMessages.userCommands;

/**
 * Forfeits the match and ends the game (no more moves can be made).
 */
public class ResignCommand extends UserGameCommand {
  private final int gameID;

  public ResignCommand(String authToken, int gameID) {
    super(authToken);
    this.gameID = gameID;
  }
}
