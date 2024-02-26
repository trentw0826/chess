package handler;


import service.RegisterService;
import service.request.RegisterRequest;
import service.response.RegisterResponse;
import spark.Request;
import spark.Response;


/**
 * Handler class acts a translator between the server's endpoint-defined HTTP requests and
 * the java objects that will be processed by the server classes.
 */
public class RegisterHandler extends Handler {

  private final RegisterService registerService = new RegisterService();

  /* Singleton implementation */
  private RegisterHandler() {}
  private static final class InstanceHolder {
    private static final RegisterHandler instance = new RegisterHandler();
  }
  public static RegisterHandler getInstance() {
    return InstanceHolder.instance;
  }


  /**
   * Handles a register request. Deserializes the request, passes the user data to the
   * user service, and re-serializes the resulting register response.
   *
   * @param sparkRequest the Spark-defined request object
   * @param sparkResponse the Spark-defined response object
   * @return    the serialized response object
   */
  @Override
  public String handleRequest(Request sparkRequest, Response sparkResponse) {
    RegisterRequest hydratedRegisterRequest = deserializeRequest(sparkRequest);
    // TODO should an error by handled here for faulty hydration?
    RegisterResponse serviceResponse = registerService.register(hydratedRegisterRequest);

    sparkResponse.status(getStatusCode(serviceResponse));

    return serializeResponse(serviceResponse);
  }


  /**
   * Deserialize the Spark request object into a UserData object.
   *
   * @param req the Spark request object
   * @return    the hydrated UserData object
   */
  protected RegisterRequest deserializeRequest(Request req) {
    return gson.fromJson(req.body(), RegisterRequest.class);
  }


  /**
   * Serialize the service's response.
   *
   * @param registerResponse service response
   * @return                the serialized service response
   */
  protected String serializeResponse(RegisterResponse registerResponse) {
    return gson.toJson(registerResponse);
  }
}
