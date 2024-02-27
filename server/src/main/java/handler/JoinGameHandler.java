package handler;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import service.JoinGameService;
import service.request.JoinGameRequest;
import service.response.JoinGameResponse;
import spark.Request;


/**
 * Handler class acts a translator between the server's endpoint-defined HTTP requests and
 * the java objects that will be processed by the server classes.
 */
public class JoinGameHandler extends Handler<JoinGameRequest, JoinGameResponse> {

  private final JoinGameService joinGameService = new JoinGameService();

  private JoinGameHandler() {}

  /* Singleton implementation */
  private static final class InstanceHolder {
    private static final JoinGameHandler instance = new JoinGameHandler();
  }
  public static JoinGameHandler instance() {
    return InstanceHolder.instance;
  }


  /**
   * Deserialize the Spark request object into a UserData object.
   *
   * @param req the Spark request object
   * @return    the hydrated UserData object
   */
  @Override
  protected JoinGameRequest deserializeRequest(Request req) {
    String authToken = req.headers("authorization");

    JsonElement playerColorElement = gson.fromJson(req.body(), JsonObject.class).get("playerColor");
    String playerColor = (playerColorElement == null) ? null : playerColorElement.getAsString();

    Integer gameID = gson.fromJson(req.body(), JsonObject.class).get("gameID").getAsInt();

    return new JoinGameRequest(authToken, playerColor, gameID);
  }

  @Override
  protected JoinGameResponse processRequest(JoinGameRequest request) {
    return joinGameService.processHandlerRequest(request);
  }
}
