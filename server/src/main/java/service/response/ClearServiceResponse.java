package service.response;

public class ClearServiceResponse extends ServiceResponse {

  // Successful clear service responses
  public ClearServiceResponse() {
    super();
  }

  // Unsuccessful user service responses
  public ClearServiceResponse(ERROR_MESSAGE errorMessage) {
    super(errorMessage);
  }
}
