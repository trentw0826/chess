package service.response;

public class LoginResponse extends ServiceResponse {
  String username;
  String authToken;

  // Successful
  public LoginResponse(String username, String authToken) {
    super();
    this.username = username;
    this.authToken = authToken;
  }

  // Unsuccessful
  public LoginResponse(String errorMessage) {
    super(errorMessage);
  }

  public String getUsername() {
    return username;
  }

  public String getAuthToken() {
    return authToken;
  }
}
