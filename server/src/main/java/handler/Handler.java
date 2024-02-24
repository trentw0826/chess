package handler;

import com.google.gson.Gson;
import service.AuthService;
import service.GameService;
import service.UserService;
import service.response.ServiceResponse;
import spark.Request;

import java.net.HttpURLConnection;

abstract class Handler {
  protected final Gson gson = new Gson();

  protected final UserService userService = new UserService();
  protected final AuthService authService = new AuthService();
  protected final GameService gameService = new GameService();

  protected abstract String handleRequest(Request req, spark.Response res);
  protected abstract Object deserializeRequest(Request req);  // TODO change this object to the parent-class of the object model
  protected abstract String serializeResponse(ServiceResponse serviceResponse);


  /**
   * Get the status code of a service response from the service response's message.
   *
   * @param serviceResponse service response object
   * @return                the status code associated with the service response's message
   */
  protected static int getStatusCode(ServiceResponse serviceResponse) {
    // TODO update the set of status codes to a map
    int responseStatus = HttpURLConnection.HTTP_OK;
    String message = serviceResponse.getMessage();

    if (!serviceResponse.isSuccess()) {
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
