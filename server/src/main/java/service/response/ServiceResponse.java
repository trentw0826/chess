package service.response;


/**
 * Holds a response to the service class
 *
 */
// TODO convert this class to a record? Alternatively, positive/negative response subclasses?
public class ServiceResponse {
  boolean success;
  String message;

  public ServiceResponse(boolean success, String message) {
    this.success = success;
    this.message = message;
  }

  public boolean isSuccess() {
    return success;
  }

  public String getMessage() {
    return message;
  }
}
