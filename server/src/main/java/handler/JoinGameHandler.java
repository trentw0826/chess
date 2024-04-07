package handler;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import playerColor.PlayerColor;
import service.JoinGameService;
import request.JoinGameRequest;
import response.JoinGameResponse;
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

    if (authToken == null) {
      throw new IllegalArgumentException("No auth token passed with request body");
    }

    JsonElement playerColorElement = gson.fromJson(req.body(), JsonObject.class).get("playerColor");
    PlayerColor playerColor;
    try {
      playerColor = PlayerColor.valueOf(playerColorElement.getAsString());
    }
    catch (IllegalArgumentException e) {
      throw new IllegalStateException("Bad player color passed to join game handler");
    }
    JsonElement gameIdElement = gson.fromJson(req.body(), JsonObject.class).get("gameID");

    if (gameIdElement == null) {
      throw new IllegalStateException("No game ID associated with requested game");
    }

    Integer gameID = gameIdElement.getAsInt();

    return new JoinGameRequest(authToken, playerColor, gameID);
  }

  @Override
  protected JoinGameResponse processRequest(JoinGameRequest request) {
    return joinGameService.processHandlerRequest(request);
  }
}
