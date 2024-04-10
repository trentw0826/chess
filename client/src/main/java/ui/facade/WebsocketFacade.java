package ui.facade;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import playerColor.PlayerColor;
import request.webSocketMessages.serverMessages.LoadGame;
import request.webSocketMessages.serverMessages.Notification;
import request.webSocketMessages.serverMessages.ServerMessage;
import request.webSocketMessages.userCommands.JoinPlayerCommand;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebsocketFacade extends Endpoint {
  private final Gson gson;
  private Session currSession;

  public WebsocketFacade(String url) throws ResponseException {
    try {
      this.gson = new Gson();

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
              //TODO Improve responsibility encapsulation by migrating printing over to UserInterface
              System.out.printf("Server notification: %s%n", notification.getMessage());
              break;
            case LOAD_GAME:
              LoadGame loadGame = gson.fromJson(s, LoadGame.class);
              ChessGame loadedGame = loadGame.getGame();
              System.out.printf("%n%s%n", loadedGame.getBoard().getPrintable(true));  //TODO factor in color
              break;
            case ERROR:
              //TODO implement logic upon receiving error from server
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


  @Override
  public void onOpen(Session session, EndpointConfig endpointConfig) {}
}
