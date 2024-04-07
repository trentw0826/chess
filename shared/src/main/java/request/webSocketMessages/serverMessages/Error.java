package request.webSocketMessages.serverMessages;

/**
 * This message is sent to a client when it sends an invalid command.
 * The message must include the word 'error'.
 */
public class Error extends ServerMessage {
  private final String errorMessage;

  public Error(ServerMessageType type, String errorMessage) {
    super(type);
    this.errorMessage = errorMessage;
  }
}
