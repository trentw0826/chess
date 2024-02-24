package handler;

import model.UserData;
import service.response.RegisterServiceResponse;
import service.response.ServiceResponse;
import spark.Request;

import java.util.Map;


/**
 * Handler class acts a translator between the server's endpoint-defined HTTP requests and
 * the java objects that will be processed by the server classes.
 */
public class RegisterHandler extends Handler {

  //TODO Does this class need to be singleton?

  /* Singleton implementation */
  private RegisterHandler() {}
  private static final class InstanceHolder {
    private static final RegisterHandler instance = new RegisterHandler();
  }
  public static RegisterHandler getInstance() {
    return InstanceHolder.instance;
  }


  /**
   * Handles a request to register a new user. Deserializes the request, passes the user data to the
   * user service, and re-serializes the resulting register response.
   *
   * @param req the Spark-defined request object
   * @param res the Spark-defined response object
   * @return    the serialized response object
   */
  @Override
  public String handleRequest(Request req, spark.Response res) {
    UserData hydratedModelData = deserializeRequest(req);
    RegisterServiceResponse serviceResponse = userService.register(hydratedModelData);

    res.status(getStatusCode(serviceResponse));

    res.type("application/json"); //TODO is this line necessary?
    return serializeResponse(serviceResponse);
  }


  /**
   * Deserialize the Spark request object into a UserData object.
   *
   * @param req the Spark request object
   * @return    the hydrated UserData object
   */
  @Override
  protected UserData deserializeRequest(Request req) {
    return gson.fromJson(req.body(), UserData.class);
  }


  /**
   * Serialize the service's response.
   *
   * @param serviceResponse service response
   * @return                the serialized service response
   */
  @Override
  protected String serializeResponse(ServiceResponse serviceResponse) {
    String jsonResponse;

    RegisterServiceResponse registerServiceResponse = (RegisterServiceResponse)serviceResponse;

    if (serviceResponse.isSuccess()) {
      jsonResponse = gson.toJson(Map.of("username", registerServiceResponse.getUsername(), "authToken", registerServiceResponse.getAuthToken()));
    }
    else {
      jsonResponse = gson.toJson(Map.of("message", serviceResponse.getMessage()));
    }

    return jsonResponse;
  }
}
