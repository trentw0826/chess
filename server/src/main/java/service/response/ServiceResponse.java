package service.response;

/**
 * Holds a response to the service class
 */
public class ServiceResponse {

  boolean success;
  String errorMessage;

  // Constructor for positive service responses
  public ServiceResponse() {
    this.success = true;
    this.errorMessage = null;
  }

  // Constructor for negative service responses
  public ServiceResponse(String errorMessage) {
    this.success = false;
    this.errorMessage = errorMessage;
  }

  /* Getters */
  public boolean isSuccess() {
    return success;
  }

  public String getErrorMessage() {
    return errorMessage;
  }
}
