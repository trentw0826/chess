package handler;


import service.ClearService;
import service.request.ClearRequest;
import service.response.ClearResponse;
import spark.Request;
import spark.Response;


/**
 * Handler class acts a translator between the server's endpoint-defined HTTP requests and
 * the java objects that will be processed by the server classes.
 */
public class ClearHandler extends Handler {

  private final ClearService clearService = new ClearService();

  /* Singleton implementation */
  private ClearHandler() {}
  private static final class InstanceHolder {
    private static final ClearHandler instance = new ClearHandler();
  }
  public static ClearHandler getInstance() {
    return InstanceHolder.instance;
  }


  /**
   * Handles a clear request. Deserializes the request into a ClearRequest object
   * passes the ClearRequest to the user service, and re-serializes the resulting clear response.
   *
   * @param sparkRequest the Spark-defined request object
   * @param sparkResponse the Spark-defined response object
   * @return    the serialized response object
   */
  @Override
  public String handleRequest(Request sparkRequest, Response sparkResponse) {
    ClearRequest hydratedClearRequest = deserializeRequest(sparkRequest);
    // TODO should an error by handled here for faulty hydration?
    ClearResponse serviceResponse = clearService.clear(hydratedClearRequest);

    sparkResponse.status(getStatusCode(serviceResponse));

    return serializeResponse(serviceResponse);
  }


  /**
   * Deserialize the Spark request object into a ClearRequest object.
   *
   * @param req the Spark request object
   * @return    the hydrated UserData object
   */
  protected ClearRequest deserializeRequest(Request req) {
    return gson.fromJson(req.body(), ClearRequest.class);
  }


  /**
   * Serialize the service's response.
   *
   * @param clearResponse   service response
   * @return                the serialized service response
   */
  protected String serializeResponse(ClearResponse clearResponse) {
    return gson.toJson(clearResponse);
  }
}
