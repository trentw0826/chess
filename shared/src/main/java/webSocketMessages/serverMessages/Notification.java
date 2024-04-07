package webSocketMessages.serverMessages;

/**
 * This is a message meant to inform a player when another player made an action.
 */
public class Notification extends ServerMessage {
  String message;

  public Notification(ServerMessageType type, String message) {
    super(type);
    this.message = message;
  }
}
