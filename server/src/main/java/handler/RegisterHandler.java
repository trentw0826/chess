package handler;

import com.google.gson.Gson;
import model.UserData;
import response.RegisterResponse;
import response.ServiceResponse;
import service.UserService;
import spark.Request;
import spark.Response;

import java.net.HttpURLConnection;
import java.util.Map;

public class RegisterHandler {
  private final Gson gson = new Gson();
  UserService userService = UserService.getInstance();

  // Private constructor for singleton implementation
  private RegisterHandler() {}

  // Singleton implementation
  private static final class InstanceHolder {
    private static final RegisterHandler instance = new RegisterHandler();
  }

  public static RegisterHandler getInstance() {
    return InstanceHolder.instance;
  }


  public String handleRequest(Request req, Response res) {
    UserData requestObject = gson.fromJson(req.body(), UserData.class);
    RegisterResponse responseObject = userService.register(requestObject);

    res.status(getStatusCode(responseObject));

    if (responseObject.isSuccess()) {
      // Registration successful
      res.type("application/json");
      return gson.toJson(Map.of("username", requestObject.username(), "authToken", responseObject.getAuthToken()));
    } else {
      // Registration failed
      res.type("application/json");
      return gson.toJson(Map.of("message", responseObject.getMessage()));
    }  }

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
