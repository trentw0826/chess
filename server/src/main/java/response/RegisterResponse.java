package response;

public class RegisterResponse extends ServiceResponse {
  String username = null;
  String authToken = null;

  public RegisterResponse(String username, String authToken) {
    super(true, null);
    this.username = username;
    this.authToken = authToken;
  }

  public RegisterResponse(boolean success, String message) {
    super(success, message);
  }

  public String getAuthToken() {
    return authToken;
  }
}
