package handler;

import com.google.gson.Gson;
import service.request.ServiceRequest;
import service.response.ServiceResponse;
import spark.Request;
import spark.Response;

import java.net.HttpURLConnection;

/**
 * Defines the functionality of the handler classes.
 * Said functionality includes receiving HTTP requests, deserializing them, passing them to corresponding
 * service classes, and re-serializing the subsequent service responses
 * @param <U>  ServiceRequest request type
 * @param <T> ServiceResponse response type
 */
abstract class Handler<U extends ServiceRequest, T extends ServiceResponse> {

  protected final Gson gson = new Gson();

  protected abstract T processRequest(U request);
  protected abstract U deserializeRequest(Request req);


  /**
   * Serialize the service's response into a JSON string
   *
   * @param serviceResponse service response
   * @return                the serialized service response
   */
  protected String serializeResponse(T serviceResponse) {
    return gson.toJson(serviceResponse);
  }


  /**
   * Get the status code of a service response from the service response's message.
   *
   * @param serviceResponse service response object
   * @return                the status code associated with the service response's message
   */
  protected int getStatusCode(T serviceResponse) {
    return serviceResponse.isSuccess() ? HttpURLConnection.HTTP_OK : getErrorCode(serviceResponse.getMessage());
  }


  /**
   * Returns error code based on the given error message.
   *
   * @param message error message
   * @return        corresponding error code
   */
  private int getErrorCode(String message) {
    if (message == null) {
      return HttpURLConnection.HTTP_INTERNAL_ERROR;
    }
    if (message.contains("bad request")) {
      return HttpURLConnection.HTTP_BAD_REQUEST;
    } else if (message.contains("unauthorized")) {
      return HttpURLConnection.HTTP_UNAUTHORIZED;
    } else if (message.contains("already taken")) {
      return HttpURLConnection.HTTP_FORBIDDEN;
    } else {
      return HttpURLConnection.HTTP_INTERNAL_ERROR;
    }
  }


  /**
   * Handles an incoming http request. Deserializes the spark request into a service request object,
   * passes request object to the corresponding service, and re-serializes the resulting service response.
   *
   * @param req the Spark-defined request object
   * @param res the Spark-defined response object
   * @return    the serialized response object
   */
  public String handleRequest(Request req, Response res) {
    U hydratedRequest = deserializeRequest(req);
    T serviceResponse = processRequest(hydratedRequest);
    res.status(getStatusCode(serviceResponse));
    return serializeResponse(serviceResponse);
  }
}
