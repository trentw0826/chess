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
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebSocket
public class WebSocketHandler {

  private static final Logger logger = Logger.getLogger(WebSocketHandler.class.getName());
  private static final Gson gson = new Gson();
  private final ConnectionManager connectionManager = new ConnectionManager();
  private final AuthDao authDao = new AuthSqlDao();
  private final GameDao gameDao = new GameSqlDao();

  @OnWebSocketMessage
  public void onMessage(Session session, String message) {
    UserGameCommand userGameCommand = new Gson().fromJson(message, UserGameCommand.class);
    switch (userGameCommand.getCommandType()) {
      case JOIN_PLAYER -> join(session, gson.fromJson(message, JoinPlayerCommand.class));
      case JOIN_OBSERVER -> observe(session, gson.fromJson(message, JoinObserverCommand.class));
//      case MAKE_MOVE -> makeMove(session, gson.fromJson(message, MakeMoveCommand.class));
//      case LEAVE -> leave(session, gson.fromJson(message, LeaveCommand.class));
//      case RESIGN -> resign(session, gson.fromJson(message, ResignCommand.class));
      default -> throw new IllegalStateException(String.format("Bad enum passed to onMessage (%s)", userGameCommand.getCommandType().toString()));
    }
  }


  private void join(Session session, JoinPlayerCommand joinPlayerCommand) {
    int desiredGameID = joinPlayerCommand.getGameID();
    String currAuthToken = joinPlayerCommand.getAuthToken();
    PlayerColor desiredColor = joinPlayerCommand.getPlayerColor();

    try {
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
      broadcast(desiredGameID, new Notification(outgoingMessage), currAuthToken);
      sendLoadGame(session, desiredGame);
    }
    catch (DataAccessException e) {
      sendError(session, e.getMessage());
    }
  }


  private void observe(Session session, JoinObserverCommand joinObserverCommand) {
    int desiredGameID = joinObserverCommand.getGameID();
    String currAuthToken = joinObserverCommand.getAuthToken();

    try {
      String requestingUsername = authDao.getUsernameFromAuthToken(currAuthToken);

      String outgoingMessage = String.format("'%s' has joined the game as an observer", requestingUsername);

      ChessGame desiredGame = gameDao.get(desiredGameID).getGame();
      connectionManager.add(desiredGameID, currAuthToken, session);
      broadcast(desiredGameID, new Notification(outgoingMessage), currAuthToken);
      sendLoadGame(session, desiredGame);
    }
    catch (DataAccessException e) {
      sendError(session, e.getMessage());
    }
  }


  private void broadcast(int gameID, Notification notification, String responsibleAuth) {
    ConcurrentMap<String, Connection> activeConnections = connectionManager.getActiveGameConnections(gameID);
    if (activeConnections != null) {
      for (Connection conn : activeConnections.values()) {
        Session session = conn.getSession();
        if (session.isOpen() && !conn.getAuthToken().equals(responsibleAuth)) {
          sendWsMessage(session, gson.toJson(notification));
        }
      }
    }
  }

  private void sendLoadGame(Session session, ChessGame game) {
    sendWsMessage(session, gson.toJson(new LoadGame(game)));
  }

  private void sendError(Session session, String message)  {
    sendWsMessage(session, gson.toJson(new Error(message)));
  }

  private static void sendWsMessage(Session session, String gson) {
    try {
      session.getRemote().sendString(gson);
    }
    catch (IOException e) {
      logger.log(Level.WARNING, "Error message couldn't be sent");
    }
  }
}
