package handler;


import service.Service;
import request.httpRequests.ServiceRequest;
import response.ServiceResponse;
import spark.Request;


/**
 * Handler class acts a translator between the server's endpoint-defined HTTP requests and
 * the java objects that will be processed by the server classes.
 */
public class ClearHandler extends Handler<ServiceRequest, ServiceResponse> {

  private final Service<ServiceRequest, ServiceResponse> service = new Service() {
    @Override
    protected ServiceResponse processHandlerRequest(ServiceRequest serviceRequest) {
      return processRequest(serviceRequest);
    }
  };

  private ClearHandler() {}

  /* Singleton implementation */
  private static final class InstanceHolder {
    private static final ClearHandler instance = new ClearHandler();
  }
  public static ClearHandler instance() {
    return InstanceHolder.instance;
  }


  /**
   * Deserialize the Spark request object into a ClearRequest object.
   *
   * @param req the Spark request object
   * @return    the hydrated UserData object
   */
  @Override
  protected ServiceRequest deserializeRequest(Request req) {
    return gson.fromJson(req.body(), ServiceRequest.class);
  }

  @Override
  protected ServiceResponse processRequest(ServiceRequest request) {
    return service.clear();
  }
}
