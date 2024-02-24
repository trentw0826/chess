package handler;

import service.response.ServiceResponse;
import spark.Request;
import spark.Response;

public class ClearHandler extends Handler {
  /* Singleton implementation */
  private ClearHandler() {}
  private static final class InstanceHolder {
    private static final ClearHandler instance = new ClearHandler();
  }
  public static ClearHandler getInstance() {
    return ClearHandler.InstanceHolder.instance;
  }

  @Override
  public String handleRequest(Request req, Response res) {
    ServiceResponse userServiceResponse = userService.clear();
    // ServiceResponse authServiceResponse = authService.clear();
    // ServiceResponse gameServiceResponse = gameService.clear();

    if (!(userServiceResponse.isSuccess()/* && authServiceResponse.isSuccess() && authServiceResponse.isSuccess()*/)) {
      throw new IllegalStateException("databases could not be cleared");
    }

    return null;
  }

  // TODO: find out how to not have to override this
  @Override
  protected Object deserializeRequest(Request req) {
    return null;
  }

  @Override
  protected String serializeResponse(ServiceResponse serviceResponse) {
    return null;
  }
}