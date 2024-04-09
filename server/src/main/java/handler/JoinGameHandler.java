package handler;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import playerColor.PlayerColor;
import service.JoinGameService;
import request.httpRequests.JoinGameRequest;
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

    JsonObject requestBody = gson.fromJson(req.body(), JsonObject.class);

    JsonElement playerColorElement = requestBody.get("playerColor");
    PlayerColor playerColor = parsePlayerColor(playerColorElement);

    JsonElement gameIdElement = requestBody.get("gameID");
    Integer gameId = parseGameID(gameIdElement);

    return new JoinGameRequest(authToken, playerColor, gameId);
  }

  private PlayerColor parsePlayerColor(JsonElement playerColorElement) {
    if (playerColorElement == null) {
      return null;
    }
    String colorString = playerColorElement.getAsString();
    try {
      return PlayerColor.valueOf(colorString);
    } catch (IllegalArgumentException e) {
      throw new IllegalStateException("Bad player color passed to join game handler");
    }
  }

  private Integer parseGameID(JsonElement gameIdElement) {
    if (gameIdElement == null || gameIdElement.isJsonNull()) {
      throw new IllegalStateException("No game ID associated with requested game");
    }
    return gameIdElement.getAsInt();
  }

  @Override
  protected JoinGameResponse processRequest(JoinGameRequest request) {
    return joinGameService.processHandlerRequest(request);
  }
}
