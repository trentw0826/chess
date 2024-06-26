package handler;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import service.CreateGameService;
import request.httpRequests.CreateGameRequest;
import response.CreateGameResponse;
import spark.Request;


/**
 * Handler class acts a translator between the server's endpoint-defined HTTP requests and
 * the java objects that will be processed by the server classes.
 */
public class CreateGameHandler extends Handler<CreateGameRequest, CreateGameResponse> {

  private final CreateGameService createGameService = new CreateGameService();

  private CreateGameHandler() {}

  /* Singleton implementation */
  private static final class InstanceHolder {
    private static final CreateGameHandler instance = new CreateGameHandler();
  }
  public static CreateGameHandler instance() {
    return InstanceHolder.instance;
  }


  /**
   * Deserialize the Spark request object into a CreateGameRequest object.
   *
   * @param req the Spark request object
   * @return    the hydrated UserData object
   */
  @Override
  protected CreateGameRequest deserializeRequest(Request req) {
    JsonElement gameNameElement = gson.fromJson(req.body(), JsonObject.class).get("gameName");

    String gameName = (gameNameElement == null) ? null : gameNameElement.getAsString();
    String authToken = req.headers("authorization");

    return new CreateGameRequest(gameName, authToken);
  }

  @Override
  protected CreateGameResponse processRequest(CreateGameRequest request) {
    return createGameService.processHandlerRequest(request);
  }
}
