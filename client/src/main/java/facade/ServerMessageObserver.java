package facade;

import request.webSocketMessages.serverMessages.ServerMessage;

public interface ServerMessageObserver {
  void notifyOfMessage(ServerMessage message);
}
