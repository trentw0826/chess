package response;

public class ClearResponse extends ServiceResponse {

  public ClearResponse() {
    super(true, null);
  }

  public ClearResponse(String message) {
    super(false, message);
  }
}
