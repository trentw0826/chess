package request.webSocketMessages.serverMessages;

import java.util.Objects;

/**
 * This is a message meant to inform a player when another player made an action.
 */
public class Notification extends ServerMessage {
  String message;

  public Notification(String message) {
    super(ServerMessageType.NOTIFICATION);
    this.message = message;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    Notification that = (Notification) o;
    return Objects.equals(message, that.message);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), message);
  }

  public String getMessage() {
    return message;
  }
}
