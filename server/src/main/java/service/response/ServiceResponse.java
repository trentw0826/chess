package service.response;

/**
 * Holds a response to the service class
 */
public class ServiceResponse {

  private final Boolean success;
  private String message;

  // Constructor for positive service responses
  public ServiceResponse() {
    success = true;
  }

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
