package service;

public class ServiceConstants {
  public enum ErrorMessages {
    BAD_REQUEST("Error: bad request"),
    UNAUTHORIZED("Error: unauthorized"),
    ALREADY_TAKEN("Error: already taken");

    private final String message;

    ErrorMessages(String message) {
      this.message = message;
    }

    public String message() {
      return message;
    }
  }
}
