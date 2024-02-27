package handler;


import service.LoginService;
import service.request.LoginRequest;
import service.response.LoginResponse;
import spark.Request;


/**
 * Handler class acts a translator between the server's endpoint-defined HTTP requests and
 * the java objects that will be processed by the server classes.
 */
public class LoginHandler extends Handler<LoginRequest, LoginResponse> {

  private final LoginService loginService = new LoginService();

  private LoginHandler() {}

  /* Singleton implementation */
  private static final class InstanceHolder {
    private static final LoginHandler instance = new LoginHandler();
  }
  public static LoginHandler getInstance() {
    return InstanceHolder.instance;
  }

  /**
   * Deserialize the Spark request object into a LoginRequest object.
   *
   * @param req the Spark request object
   * @return    the hydrated LoginRequest object
   */
  @Override
  protected LoginRequest deserializeRequest(Request req) {
    return gson.fromJson(req.body(), LoginRequest.class);
  }

  @Override
  protected LoginResponse processRequest(LoginRequest request) {
    return loginService.processHandlerRequest(request);
  }
}
