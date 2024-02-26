package service.response;


public class LogoutResponse extends ServiceResponse {

  // Successful
  public LogoutResponse() {
    super();
  }

  // Unsuccessful
  public LogoutResponse(String errorMessage) {
    super(errorMessage);
  }

}
