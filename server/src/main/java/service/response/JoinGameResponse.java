package service.response;

public class JoinGameResponse extends ServiceResponse {

  // Successful
  public JoinGameResponse() {
    super();
  }

  // Unsuccessful
  public JoinGameResponse(String errorMessage) {
    super(errorMessage);
  }
}
