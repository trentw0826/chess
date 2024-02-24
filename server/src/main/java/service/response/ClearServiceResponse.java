package service.response;


public class ClearServiceResponse extends ServiceResponse {

  public ClearServiceResponse() {
    super(true, null);
  }

  public ClearServiceResponse(String message) {
    super(false, message);
  }
}
