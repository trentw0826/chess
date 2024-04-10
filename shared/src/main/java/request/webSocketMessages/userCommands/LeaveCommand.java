package request.webSocketMessages.userCommands;

import java.util.Objects;

/**
 * Tells the server you are leaving the game so it will stop sending you notifications.
 */
public class LeaveCommand extends UserGameCommand {
  private final int gameID;

  public LeaveCommand(String authToken, int gameID) {
    super(authToken);
    this.commandType = CommandType.LEAVE;
    this.gameID = gameID;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    LeaveCommand that = (LeaveCommand) o;
    return gameID == that.gameID;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), gameID);
  }
}
