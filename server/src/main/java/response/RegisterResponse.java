package response;

public class RegisterResponse extends ServiceResponse {
  String username;
  String authToken;

  public RegisterResponse(String username, String authToken) {
    super(true, "idk"); //TODO success message
    this.username = username;
    this.authToken = authToken;
  }
}
