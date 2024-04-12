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
import request.webSocketMessages.userCommands.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import static request.webSocketMessages.userCommands.UserGameCommand.CommandType.*;

@WebSocket
public class WebSocketHandler {

  private static final Logger logger = Logger.getLogger(WebSocketHandler.class.getName());
  private static final Gson gson = new Gson();
  private final ConnectionManager connectionManager = new ConnectionManager();
  private final AuthDao authDao = new AuthSqlDao();
  private final GameDao gameDao = new GameSqlDao();

  private final Map<UserGameCommand.CommandType, CommandHandler> commandHandlers = new HashMap<>();

  public WebSocketHandler() {
    commandHandlers.put(JOIN_PLAYER, this::join);
    commandHandlers.put(JOIN_OBSERVER, this::observe);
//    commandHandlers.put(MAKE_MOVE, this::makeMove);
    commandHandlers.put(LEAVE, this::leave);
//    commandHandlers.put(RESIGN, this::resign);
  }

  @OnWebSocketMessage
  public void onMessage(Session session, String message) {
    UserGameCommand userGameCommand = gson.fromJson(message, UserGameCommand.class);
    commandHandlers.get(userGameCommand.getCommandType()).handle(session, message);
  }

  private interface CommandHandler {
    void handle(Session session, String message);
  }

  private void join(Session session, String message) {
    JoinPlayerCommand joinPlayerCommand = gson.fromJson(message, JoinPlayerCommand.class);
    int desiredGameID = joinPlayerCommand.getGameID();
    String currAuthToken = joinPlayerCommand.getAuthToken();
    PlayerColor desiredColor = joinPlayerCommand.getPlayerColor();

    try {
      String requestingUsername = authDao.getUsernameFromAuthToken(currAuthToken);
      GameData desiredGameData = gameDao.get(desiredGameID);
      String existingUsername = (desiredColor == PlayerColor.WHITE) ? desiredGameData.getWhiteUsername() : desiredGameData.getBlackUsername();

      if (!(existingUsername != null && existingUsername.equals(requestingUsername))) {
        sendError(session, String.format("Error: %s is already taken by %s", desiredColor.toString(), existingUsername));
        return;
      }

      String outgoingMessage = String.format("'%s' has joined the game as %s", requestingUsername, desiredColor);
      ChessGame desiredGame = desiredGameData.getGame();
      connectionManager.add(desiredGameID, currAuthToken, session);
      broadcast(desiredGameID, new Notification(outgoingMessage), currAuthToken);
      sendLoadGame(session, desiredGame);
    }
    catch (DataAccessException e) {
      sendError(session, e.getMessage());
    }
  }

  private void observe(Session session, String message) {
    JoinObserverCommand joinObserverCommand = gson.fromJson(message, JoinObserverCommand.class);
    int desiredGameID = joinObserverCommand.getGameID();
    String currAuthToken = joinObserverCommand.getAuthToken();

    try {
    String requestingUsername = authDao.getUsernameFromAuthToken(currAuthToken);
      GameData desiredGameData = gameDao.get(desiredGameID);
      String outgoingMessage = String.format("'%s' has joined the game as an observer", requestingUsername);
      ChessGame desiredGame = desiredGameData.getGame();
      connectionManager.add(desiredGameID, currAuthToken, session);
      broadcast(desiredGameID, new Notification(outgoingMessage), currAuthToken);
      sendLoadGame(session, desiredGame);
    }
    catch (DataAccessException e) {
      sendError(session, e.getMessage());
    }
  }

  private void leave(Session session, String message) {
    LeaveCommand leaveCommand = gson.fromJson(message, LeaveCommand.class);
    int desiredGameID = leaveCommand.getGameID();
    String currAuthToken = leaveCommand.getAuthToken();

    try {
      String requestingUsername = authDao.getUsernameFromAuthToken(currAuthToken);

      String outgoingMessage = String.format("'%s' has left the game", requestingUsername);
      connectionManager.remove(desiredGameID, currAuthToken);
      broadcast(desiredGameID, new Notification(outgoingMessage), currAuthToken);
    }
    catch (DataAccessException e) {
      sendError(session, e.getMessage());
    }
  }

  private void broadcast(int gameID, Notification notification, String responsibleAuth) {
    ConcurrentMap<String, Connection> activeConnections = connectionManager.getActiveGameConnections(gameID);
    if (activeConnections != null) {
      activeConnections.values().stream()
              .filter(conn -> conn.getSession().isOpen() && !conn.getAuthToken().equals(responsibleAuth))
              .map(Connection::getSession)
              .forEach(session -> sendWsMessage(session, gson.toJson(notification)));
    }
  }

  private void sendLoadGame(Session session, ChessGame game) {
    sendWsMessage(session, gson.toJson(new LoadGame(game)));
  }

  private void sendError(Session session, String message) {
    sendWsMessage(session, gson.toJson(new Error(message)));
  }

  private static void sendWsMessage(Session session, String message) {
    try {
      session.getRemote().sendString(message);
    } catch (IOException e) {
      logger.log(Level.WARNING, "Error message couldn't be sent");
    }
  }
}
