package handler;

import com.google.gson.Gson;
import response.ClearResponse;
import response.ServiceResponse;
import service.Service;
import spark.Request;
import spark.Response;

import java.net.HttpURLConnection;
import java.util.Map;

public class ClearHandler {

  private final Gson gson = new Gson();
  Service service = new Service();

  // Private constructor for singleton implementation
  private ClearHandler() {}

  // Singleton implementation
  private static final class InstanceHolder {
    private static final ClearHandler instance = new ClearHandler();
  }

  public static ClearHandler getInstance() {
    return ClearHandler.InstanceHolder.instance;
  }


  public String handleRequest(Request req, Response res) {
    ClearResponse responseObject = service.clearAllLocalDatabases();

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

  private static int getStatusCode(ServiceResponse responseObject) {
    int responseStatus = HttpURLConnection.HTTP_OK;
    String message = responseObject.getMessage();

    if (!responseObject.isSuccess()) {
      if (message.contains("bad request")) {
        responseStatus = HttpURLConnection.HTTP_BAD_REQUEST;
      }
      else if (message.contains("already taken")) {
        responseStatus = HttpURLConnection.HTTP_FORBIDDEN;
      }
      else {
        responseStatus = HttpURLConnection.HTTP_INTERNAL_ERROR;
      }
    }
    return responseStatus;
  }
}
