package handler;


import service.LoginService;
import service.request.LoginRequest;
import service.response.LoginResponse;
import spark.Request;
import spark.Response;

import java.util.Map;


/**
 * Handler class acts a translator between the server's endpoint-defined HTTP requests and
 * the java objects that will be processed by the server classes.
 */
public class LoginHandler extends Handler {

  private final LoginService loginService = new LoginService();

  //TODO Does this class need to be singleton?

  /* Singleton implementation */
  private LoginHandler() {}
  private static final class InstanceHolder {
    private static final LoginHandler instance = new LoginHandler();
  }
  public static LoginHandler getInstance() {
    return InstanceHolder.instance;
  }


  /**
   * Handles a login request. Deserializes the request, passes the user data to the
   * user service, and re-serializes the resulting login response.
   *
   * @param sparkRequest the Spark-defined request object
   * @param sparkResponse the Spark-defined response object
   * @return    the serialized response object
   */
  public String handleRequest(Request sparkRequest, Response sparkResponse) {
    LoginRequest hydratedLoginRequest = deserializeRequest(sparkRequest);
    // TODO should an error by handled here for faulty hydration?
    LoginResponse serviceResponse = loginService.login(hydratedLoginRequest);

    sparkResponse.status(getStatusCode(serviceResponse));

    return serializeResponse(serviceResponse);
  }


  /**
   * Deserialize the Spark request object into a UserData object.
   *
   * @param req the Spark request object
   * @return    the hydrated UserData object
   */
  protected LoginRequest deserializeRequest(Request req) {
    return gson.fromJson(req.body(), LoginRequest.class);
  }


  /**
   * Serialize the service's response.
   *
   * @param loginResponse service response
   * @return                the serialized service response
   */
  protected String serializeResponse(LoginResponse loginResponse) {
    String jsonResponse;

    if (loginResponse.isSuccess()) {
      jsonResponse = gson.toJson(Map.of("username", loginResponse.getUsername(),  "authToken", loginResponse.getAuthToken().authToken()));
    }
    else {
      jsonResponse = gson.toJson(Map.of("message", loginResponse.getErrorMessage()));
    }

    return jsonResponse;
  }
}
