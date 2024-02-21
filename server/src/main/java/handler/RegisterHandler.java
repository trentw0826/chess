package handler;

import com.google.gson.Gson;
import request.RegisterRequest;
import response.RegisterResponse;
import service.RegisterService;
import spark.Request;
import spark.Response;

public class RegisterHandler {
  private final Gson gson = new Gson();

  // Private constructor for singleton implementation
  private RegisterHandler() {}

  private static final class InstanceHolder {
    private static final RegisterHandler instance = new RegisterHandler();
  }

  public static RegisterHandler getInstance() {
    return InstanceHolder.instance;
  }

  public String handleRequest(Request req, Response res) {
    RegisterRequest request = gson.fromJson(req.body(), RegisterRequest.class);

    RegisterService service = new RegisterService();
    RegisterResponse result = service.register(request);

    return gson.toJson(result);
  }
}
