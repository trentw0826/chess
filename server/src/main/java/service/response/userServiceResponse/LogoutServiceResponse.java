package service.response.userServiceResponse;

import service.response.ServiceResponse;

public class LogoutServiceResponse extends ServiceResponse {
  String authToken;

  // Successful login service responses
  public LogoutServiceResponse(String authToken) {
    super();
    this.authToken = authToken;
  }

  // Unsuccessful login service responses
  public LogoutServiceResponse(ERROR_MESSAGE errorMessage) {
    super(errorMessage);
  }

  public String getAuthToken() {
    return authToken;
  }
}
