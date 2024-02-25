package service.response;


public class ClearResponse extends ServiceResponse {

  // Successful
  public ClearResponse() {
    super();
  }

  // Unsuccessful
  public ClearResponse(String errorMessage) {
    super(errorMessage);
  }

}
