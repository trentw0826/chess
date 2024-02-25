package service.response;

import model.AuthToken;

import java.util.Objects;

public class LoginResponse extends ServiceResponse {
  String username;
  AuthToken authToken;

  // Successful user service responses
  public LoginResponse(String username, AuthToken authToken) {
    super();
    this.username = username;
    this.authToken = authToken;
  }

  // Unsuccessful user service responses
  public LoginResponse(String errorMessage) {
    super(errorMessage);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    LoginResponse that = (LoginResponse) o;
    return Objects.equals(username, that.username) && Objects.equals(authToken, that.authToken);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username, authToken);
  }

  public String getUsername() {
    return username;
  }

  public AuthToken getAuthToken() {
    return authToken;
  }
}
