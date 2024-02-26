package handler;


import service.LogoutService;
import service.request.LogoutRequest;
import service.response.LogoutResponse;
import spark.Request;
import spark.Response;


/**
 * Handler class acts a translator between the server's endpoint-defined HTTP requests and
 * the java objects that will be processed by the server classes.
 */
public class LogoutHandler extends Handler {

  private final LogoutService logoutService = new LogoutService();

  /* Singleton implementation */
  private LogoutHandler() {}
  private static final class InstanceHolder {
    private static final LogoutHandler instance = new LogoutHandler();
  }
  public static LogoutHandler getInstance() {
    return InstanceHolder.instance;
  }


  /**
   * Handles a logout request. Deserializes the request into LogoutRequest object, passes
   * request to the user service, and re-serializes the resulting LogoutResponse object.
   *
   * @param sparkRequest  the Spark-defined request object
   * @param sparkResponse the Spark-defined response object
   * @return              the serialized response object
   */
  @Override
  public String handleRequest(Request sparkRequest, Response sparkResponse) {
    LogoutRequest hydratedLogoutRequest = deserializeRequest(sparkRequest);
    LogoutResponse serviceResponse = logoutService.logout(hydratedLogoutRequest);

    sparkResponse.status(getStatusCode(serviceResponse));

    return serializeResponse(serviceResponse);
  }


  /**
   * Deserialize the Spark request object into a LogoutRequest object.
   *
   * @param req the Spark request object
   * @return    the hydrated LogoutRequest object
   */
  private LogoutRequest deserializeRequest(Request req) {
    String authToken = req.headers("authorization");
    return (new LogoutRequest(authToken));
  }


  /**
   * Serialize the service's response.
   *
   * @param logoutResponse  service response
   * @return                the serialized service response
   */
  private String serializeResponse(LogoutResponse logoutResponse) {
    return gson.toJson(logoutResponse);
  }
}
