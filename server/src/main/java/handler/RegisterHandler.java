package handler;


import service.RegisterService;
import service.request.RegisterRequest;
import service.response.RegisterResponse;
import spark.Request;


/**
 * Handler class acts a translator between the server's endpoint-defined HTTP requests and
 * the java objects that will be processed by the server classes.
 */
public class RegisterHandler extends Handler<RegisterRequest, RegisterResponse> {

  private final RegisterService registerService = new RegisterService();

  private RegisterHandler() {}

  /* Singleton implementation */
  private static final class InstanceHolder {
    private static final RegisterHandler instance = new RegisterHandler();
  }
  public static RegisterHandler instance() {
    return InstanceHolder.instance;
  }


  /**
   * Deserialize the Spark request object into a UserData object.
   *
   * @param req the Spark request object
   * @return    the hydrated UserData object
   */
  @Override
  protected RegisterRequest deserializeRequest(Request req) {
    return gson.fromJson(req.body(), RegisterRequest.class);
  }

  @Override
  protected RegisterResponse processRequest(RegisterRequest request) {
    return registerService.processHandlerRequest(request);
  }
}
