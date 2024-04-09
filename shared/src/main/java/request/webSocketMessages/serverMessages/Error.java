package request.webSocketMessages.serverMessages;

import java.util.Objects;

/**
 * This message is sent to a client when it sends an invalid command.
 * The message must include the word 'error'.
 */
public class Error extends ServerMessage {
  private final String errorMessage;

  public Error(String errorMessage) {
    super(ServerMessageType.ERROR);
    this.errorMessage = errorMessage;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    Error error = (Error) o;
    return Objects.equals(errorMessage, error.errorMessage);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), errorMessage);
  }
}
