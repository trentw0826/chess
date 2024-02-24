package service.response.userServiceResponse;

import service.response.ServiceResponse;

import java.util.Objects;

public class LoginServiceResponse extends ServiceResponse {
  String username;
  String authToken;

  // Successful login service responses
  public LoginServiceResponse(String username, String authToken) {
    super();
    this.username = username;
    this.authToken = authToken;
  }

  // Unsuccessful login service responses
  public LoginServiceResponse(ERROR_MESSAGE errorMessage) {
    super(errorMessage);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    LoginServiceResponse that = (LoginServiceResponse) o;
    return Objects.equals(username, that.username) && Objects.equals(authToken, that.authToken);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username, authToken);
  }

  public String getUsername() {
    return username;
  }

  public String getAuthToken() {
    return authToken;
  }
}
