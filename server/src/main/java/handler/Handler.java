package handler;

import com.google.gson.Gson;
import service.response.ServiceResponse;

import java.net.HttpURLConnection;

/**
 * Request handler abstract parent class.
 */
abstract class Handler {
  protected final Gson gson = new Gson();


  /**
   * Get the status code of a service response from the service response's message.
   *
   * @param serviceResponse service response object
   * @return                the status code associated with the service response's message
   */
  protected static int getStatusCode(ServiceResponse serviceResponse) {
    int responseStatus;

    String message = serviceResponse.getErrorMessage();

    if (serviceResponse.isSuccess()) {
      responseStatus = HttpURLConnection.HTTP_OK;
    }
    else {
      if (message.contains("bad request")) {
        responseStatus = HttpURLConnection.HTTP_BAD_REQUEST;
      }
      else if (message.contains("unauthorized")) {
        responseStatus = HttpURLConnection.HTTP_UNAUTHORIZED;
      }
      else if (message.contains("already taken")) {
        responseStatus = HttpURLConnection.HTTP_FORBIDDEN;
      }
      else {
        responseStatus = HttpURLConnection.HTTP_INTERNAL_ERROR;
      }
    }
    return responseStatus;
  }
}
