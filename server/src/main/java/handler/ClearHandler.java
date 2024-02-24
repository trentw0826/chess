package handler;

import service.Service;
import service.response.ClearServiceResponse;
import service.response.ServiceResponse;
import spark.Request;

import java.util.Map;

public class ClearHandler extends Handler {

  Service service = new Service();

  /* Singleton implementation */
  private ClearHandler() {}
  private static final class InstanceHolder {
    private static final ClearHandler instance = new ClearHandler();
  }
  public static ClearHandler getInstance() {
    return ClearHandler.InstanceHolder.instance;
  }

  @Override
  public String handleRequest(Request req, spark.Response res) {
    ClearServiceResponse responseObject = service.clearAllLocalDatabases();

    res.status(getStatusCode(responseObject));
    String responseString = null;

    if (responseObject.isSuccess()) {
      // Registration successful
      res.type("application/json");
      responseString = "";
    }
    else {
      // Registration failed
      res.type("application/json");
      responseString = gson.toJson(Map.of("message", responseObject.getMessage()));
    }

    return responseString;
  }

  @Override
  protected Object deserializeRequest(Request req) {
    return null;
  }

  @Override
  protected String serializeResponse(ServiceResponse serviceResponse) {
    return null;
  }
}
