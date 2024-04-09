package server.websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ConnectionManager {
  private final ConcurrentMap<Integer, ConcurrentHashMap<String, Connection>> connections = new ConcurrentHashMap<>();

  public void add(int gameID, String authToken, Session session) {
    ConcurrentMap<String, Connection> activeGameConnections = getActiveGameConnections(gameID);

    if (activeGameConnections != null) {
      // Game has at least one active connection
      Connection updatedConnection = new Connection(authToken, session);
      activeGameConnections.put(authToken, updatedConnection);
    }
    else {
      // Game has no connected players or observers
      Connection newConnection = new Connection(authToken, session);
      ConcurrentHashMap<String, Connection> newActiveGameConnections = new ConcurrentHashMap<>();
      newActiveGameConnections.put(authToken, newConnection);
      connections.put(gameID, newActiveGameConnections);
    }
  }

  public ConcurrentMap<String, Connection> getActiveGameConnections(int gameID) {
    return connections.get(gameID);
  }
}
