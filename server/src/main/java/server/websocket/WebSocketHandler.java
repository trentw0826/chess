package server.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.dataAccessObject.AuthDao;
import dataAccess.dataAccessObject.GameDao;
import dataAccess.sqlAccess.sqlAccessObjects.AuthSqlDao;
import dataAccess.sqlAccess.sqlAccessObjects.GameSqlDao;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import playerColor.PlayerColor;
import request.webSocketMessages.serverMessages.*;
import request.webSocketMessages.serverMessages.Error;
import request.webSocketMessages.userCommands.JoinPlayerCommand;
import request.webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;
import java.util.concurrent.ConcurrentMap;

@WebSocket
public class WebSocketHandler {

  private final Gson gson;
  private final ConnectionManager connectionManager;
  private final AuthDao authDao;
  private final GameDao gameDao;

  public WebSocketHandler() {
    gson = new Gson();
    this.connectionManager = new ConnectionManager();
    this.authDao = new AuthSqlDao();
    this.gameDao = new GameSqlDao();
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
    try {
      int desiredGameID = joinPlayerCommand.getGameID();
      String currAuthToken = joinPlayerCommand.getAuthToken();
      PlayerColor desiredColor = joinPlayerCommand.getPlayerColor();

      String requestingUsername = authDao.getUsernameFromAuthToken(currAuthToken);
      GameData desiredGameData = gameDao.get(desiredGameID);
      String existingUsername = (desiredColor == PlayerColor.WHITE) ?
              desiredGameData.getWhiteUsername() : desiredGameData.getBlackUsername();

      if (!(existingUsername != null && existingUsername.equals(requestingUsername))) {
        sendError(session, String.format("Error: %s is already taken by %s", desiredColor.toString(), existingUsername));
        return;
      }

      String outgoingMessage = String.format("'%s' has joined the game as %s",
              requestingUsername, desiredColor.toString());

      ChessGame desiredGame = desiredGameData.getGame();
      connectionManager.add(desiredGameID, currAuthToken, session);
      Notification notification = new Notification(outgoingMessage);
      broadcast(desiredGameID, notification, currAuthToken);
      sendLoadGame(session, desiredGame);
    }
    catch (IOException | DataAccessException e) {
      throw new IllegalStateException(e);
    }
  }

  private void broadcast(int gameID, Notification notification, String responsibleAuth) throws IOException {
    ConcurrentMap<String, Connection> activeConnections = connectionManager.getActiveGameConnections(gameID);
    if (activeConnections != null) {
      for (Connection conn : activeConnections.values()) {
        if (conn.getSession().isOpen() && !conn.getAuthToken().equals(responsibleAuth)) {
          conn.send(gson.toJson(notification));
        }
      }
    }
  }

  private void sendLoadGame(Session session, ChessGame game) throws IOException {
    if (session.isOpen()) {
      session.getRemote().sendString(gson.toJson(new LoadGame(game)));
    }
  }

  private void sendError(Session session, String message) throws IOException {
    if (session.isOpen()) {
      session.getRemote().sendString(gson.toJson(new Error(message)));
    }
  }
}
