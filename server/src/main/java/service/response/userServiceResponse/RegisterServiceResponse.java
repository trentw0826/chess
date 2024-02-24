package service.response.userServiceResponse;

import service.response.ServiceResponse;

import java.util.Objects;

public class RegisterServiceResponse extends ServiceResponse {
  String username;
  String authToken;

  // Successful user service responses
  public RegisterServiceResponse(String username, String authToken) {
    super();
    this.username = username;
    this.authToken = authToken;
  }

  // Unsuccessful user service responses
  public RegisterServiceResponse(ERROR_MESSAGE errorMessage) {
    super(errorMessage);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    RegisterServiceResponse that = (RegisterServiceResponse) o;
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
