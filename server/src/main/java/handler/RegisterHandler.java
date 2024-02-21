package handler;

import com.google.gson.Gson;
import model.UserData;
import response.ServiceResponse;
import service.UserService;
import spark.Request;
import spark.Response;

public class RegisterHandler {
  private final Gson gson = new Gson();

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
    UserData request = gson.fromJson(req.body(), UserData.class);

    UserService userService = new UserService();

    ServiceResponse result = userService.register(request);

    return gson.toJson(result);
  }
}
