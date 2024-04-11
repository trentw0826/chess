package request.webSocketMessages.userCommands;

import java.util.Objects;

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

  public int getGameID() {
    return gameID;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    JoinObserverCommand that = (JoinObserverCommand) o;
    return gameID == that.gameID;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), gameID);
  }
}
