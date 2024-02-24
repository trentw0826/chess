package service.response;

import java.util.Objects;

public class RegisterServiceResponse extends ServiceResponse {
  String username = null;
  String authToken = null;


  public RegisterServiceResponse(String username, String authToken) {
    super(true, null);
    this.username = username;
    this.authToken = authToken;
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


  public RegisterServiceResponse(boolean success, String message) {
    super(success, message);
  }


  public String getUsername() {
    return username;
  }

  public String getAuthToken() {
    return authToken;
  }
}