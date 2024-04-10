package server.websocket;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.dataAccessObject.AuthDao;
import dataAccess.dataAccessObject.GameDao;
import dataAccess.sqlAccess.sqlAccessObjects.AuthSqlDao;
import dataAccess.sqlAccess.sqlAccessObjects.GameSqlDao;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import playerColor.PlayerColor;
import request.webSocketMessages.serverMessages.*;
import request.webSocketMessages.userCommands.JoinPlayerCommand;
import request.webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;
import java.util.concurrent.ConcurrentMap;

@WebSocket
public class WebSocketHandler {

  private final Gson gson;
  private final ConnectionManager connectionManager;
  private final AuthDao authDao;

  public WebSocketHandler() {
    gson = new Gson();
    this.connectionManager = new ConnectionManager();
    this.authDao = new AuthSqlDao();
  }

  @OnWebSocketMessage
  public void onMessage(Session session, String message) {
    UserGameCommand userGameCommand = new Gson().fromJson(message, UserGameCommand.class);
    switch (userGameCommand.getCommandType()) {
      case JOIN_PLAYER -> join(gson.fromJson(message, JoinPlayerCommand.class), session);
//        case JOIN_OBSERVER -> observe(conn, message);
//        case MAKE_MOVE -> move(conn, message);
//        case LEAVE -> leave(conn, message);
//        case RESIGN -> resign(conn, message);
      default -> throw new IllegalStateException("Bad enum passed to onMessage");
    }
  }

  private void join(JoinPlayerCommand joinPlayerCommand, Session session) {
    int desiredGameID = joinPlayerCommand.getGameID();
    String currAuthToken = joinPlayerCommand.getAuthToken();
    PlayerColor desiredColor = joinPlayerCommand.getPlayerColor();

    connectionManager.add(desiredGameID, currAuthToken, session);

    String requestingUsername;
    try {
      requestingUsername = authDao.getUsernameFromAuthToken(currAuthToken);

      String outgoingMessage = String.format("'%s' has joined the game as %s",
              requestingUsername, desiredColor.toString());

      Notification notification = new Notification(outgoingMessage);
      broadcast(desiredGameID, notification, currAuthToken);
    }
    catch (DataAccessException e) {
      //TODO send error message to requesting connection
    }
  }

  private void broadcast(int gameID, Notification notification, String responsibleAuth) {
    ConcurrentMap<String, Connection> activeConnections = connectionManager.getActiveGameConnections(gameID);
    if (activeConnections != null) {
      for (Connection conn : activeConnections.values()) {
        if (conn.getSession().isOpen() && !conn.getAuthToken().equals(responsibleAuth)) {
          try {
            conn.send(gson.toJson(notification));
          }
          catch (IOException e) {
            // TODO how should broadcast handle not being able to send a notification?
          }
        }
      }
    }
  }
}
