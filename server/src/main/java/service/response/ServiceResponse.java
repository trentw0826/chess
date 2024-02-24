package service.response;


import java.util.EnumMap;
import java.util.Map;

/**
 * Holds a response to the service class
 */
public class ServiceResponse {

  public enum ERROR_MESSAGE {
    ERROR_ALREADY_TAKEN,
    ERROR_BAD_REQUEST,
    ERROR_UNAUTHORIZED
  }

  public static final Map<ERROR_MESSAGE, String> errorMessageToString = new EnumMap<>(ERROR_MESSAGE.class);

  static {
    errorMessageToString.put(ERROR_MESSAGE.ERROR_ALREADY_TAKEN, "Error: already taken");
    errorMessageToString.put(ERROR_MESSAGE.ERROR_BAD_REQUEST, "Error: bad request");
    errorMessageToString.put(ERROR_MESSAGE.ERROR_UNAUTHORIZED, "Error: unauthorized");
  }

  boolean success;
  ERROR_MESSAGE errorMessage;

  // Constructor for positive service responses
  public ServiceResponse() {
    this.success = true;
    this.errorMessage = null;
  }

  // Constructor for negative service responses
  public ServiceResponse(ERROR_MESSAGE errorMessage) {
    this.success = false;
    this.errorMessage = errorMessage;
  }

  /* Getters */
  public boolean isSuccess() {
    return success;
  }

  public ERROR_MESSAGE getErrorMessage() {
    return errorMessage;
  }
}
