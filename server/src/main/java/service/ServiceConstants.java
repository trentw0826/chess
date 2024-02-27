package service;

public class ServiceConstants {
  public enum ERROR_MESSAGES {
    BAD_REQUEST("Error: bad request"),
    UNAUTHORIZED("Error: unauthorized"),
    ALREADY_TAKEN("Error: already taken");

    private final String message;

    ERROR_MESSAGES(String message) {
      this.message = message;
    }

    public String message() {
      return message;
    }
  }
}
