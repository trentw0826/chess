package handler;


import service.ListGamesService;
import request.httpRequests.ListGamesRequest;
import response.ListGamesResponse;
import spark.Request;


/**
 * Handler class acts a translator between the server's endpoint-defined HTTP requests and
 * the java objects that will be processed by the server classes.
 */
public class ListGamesHandler extends Handler<ListGamesRequest, ListGamesResponse> {

  private final ListGamesService listGamesService = new ListGamesService();

  private ListGamesHandler() {}

  /* Singleton implementation */
  private static final class InstanceHolder {
    private static final ListGamesHandler instance = new ListGamesHandler();
  }
  public static ListGamesHandler instance() {
    return InstanceHolder.instance;
  }


  /**
   * Deserialize the Spark request object into a GameData object.
   *
   * @param req the Spark request object
   * @return    the hydrated UserData object
   */
  @Override
  protected ListGamesRequest deserializeRequest(Request req) {
    return new ListGamesRequest(req.headers("authorization"));
  }

  @Override
  protected ListGamesResponse processRequest(ListGamesRequest request) {
    return listGamesService.processHandlerRequest(request);
  }
}
