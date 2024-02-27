package service.response;

/**
 * Holds a response to the service class
 */
public class ServiceResponse {

  //TODO understand and optimize the logic of 'success' Boolean
  Boolean success;
  String message;

  // Constructor for positive service responses
  public ServiceResponse() {}

  // Constructor for negative service responses
  public ServiceResponse(String message) {
    this.success = false;
    this.message = message;
  }

  /* Getters */
  public boolean isSuccess() {
    return (success == null || Boolean.TRUE.equals(success));
  }

  public String getMessage() {
    return message;
  }
}
