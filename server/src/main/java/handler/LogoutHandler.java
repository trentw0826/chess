package handler;


import service.LogoutService;
import service.request.LogoutRequest;
import service.response.LogoutResponse;
import spark.Request;


/**
 * Handler class acts a translator between the server's endpoint-defined HTTP requests and
 * the java objects that will be processed by the server classes.
 */
public class LogoutHandler extends Handler<LogoutRequest, LogoutResponse> {

  private final LogoutService logoutService = new LogoutService();

  private LogoutHandler() {}

  /* Singleton implementation */
  private static final class InstanceHolder {
    private static final LogoutHandler instance = new LogoutHandler();
  }
  public static LogoutHandler getInstance() {
    return InstanceHolder.instance;
  }

  /**
   * Deserialize the Spark request object into a LogoutRequest object.
   *
   * @param req the Spark request object
   * @return    the hydrated LogoutRequest object
   */
  protected LogoutRequest deserializeRequest(Request req) {
    String authToken = req.headers("authorization");
    return (new LogoutRequest(authToken));
  }

  @Override
  protected LogoutResponse processRequest(LogoutRequest request) {
    return logoutService.processHandlerRequest(request);
  }
}
