package service.response;

public class RegisterResponse extends ServiceResponse {
  String username;
  String authToken;

  // Successful
  public RegisterResponse(String username, String authToken) {
    super();
    this.username = username;
    this.authToken = authToken;
  }

  // Unsuccessful
  public RegisterResponse(String errorMessage) {
    super(errorMessage);
  }

  public String getUsername() {
    return username;
  }

  public String getAuthToken() {
    return authToken;
  }
}
