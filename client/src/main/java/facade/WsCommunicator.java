package facade;

import com.google.gson.Gson;
import exception.ResponseException;
import playerColor.PlayerColor;
import request.webSocketMessages.serverMessages.Error;
import request.webSocketMessages.serverMessages.LoadGame;
import request.webSocketMessages.serverMessages.Notification;
import request.webSocketMessages.serverMessages.ServerMessage;
import request.webSocketMessages.userCommands.JoinPlayerCommand;
import request.webSocketMessages.userCommands.LeaveCommand;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WsCommunicator extends Endpoint {
  private final Gson gson;
  private final Session currSession;

  public WsCommunicator(String url, ServerMessageObserver serverMessageObserver) throws ResponseException {
    this.gson = new Gson();

    try {
      url = url.replace("http", "ws");
      URI socketURI = new URI(url + "/connect");

      WebSocketContainer container = ContainerProvider.getWebSocketContainer();
      this.currSession = container.connectToServer(this, socketURI);

      this.currSession.addMessageHandler(new MessageHandler.Whole<String>() {
        @Override
        public void onMessage(String s) {
          ServerMessage.ServerMessageType messageType = gson.fromJson(s, ServerMessage.class).getServerMessageType();
          switch (messageType) {
            case NOTIFICATION:
              Notification notification = gson.fromJson(s, Notification.class);
              serverMessageObserver.notifyOfMessage(notification);
              break;

            case LOAD_GAME:
              LoadGame loadGame = gson.fromJson(s, LoadGame.class);
              serverMessageObserver.notifyOfMessage(loadGame);
              break;

            case ERROR:
              Error errorMessage = gson.fromJson(s, Error.class);
              serverMessageObserver.notifyOfMessage(errorMessage);
              break;

            default:
              throw new IllegalStateException("Bad enum passed to message handler");
          }
        }
      });
    }
    catch (URISyntaxException | DeploymentException | IOException e) {
      throw new ResponseException(e.getMessage(), 500);
    }
  }


  public void joinPlayer(int gameID, String authToken, PlayerColor color) throws ResponseException {
    JoinPlayerCommand joinPlayerCommand = new JoinPlayerCommand(authToken, gameID, color);
    try {
      this.currSession.getBasicRemote().sendText(gson.toJson(joinPlayerCommand));
    }
    catch (IOException e) {
      throw new ResponseException(e.getMessage(), 500);
    }
  }

  public void leave(int gameID, String authToken) throws ResponseException {
    LeaveCommand leaveCommand = new LeaveCommand(authToken, gameID);
    try {
      this.currSession.getBasicRemote().sendText(gson.toJson(leaveCommand));
    }
    catch (IOException e) {
      throw new ResponseException(e.getMessage(), 500);
    }
  }


  @Override
  public void onOpen(Session session, EndpointConfig endpointConfig) {}
}
