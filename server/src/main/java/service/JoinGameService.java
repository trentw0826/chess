package service;

import dataAccess.DataAccessException;
import model.GameData;
import request.httpRequests.JoinGameRequest;
import response.JoinGameResponse;
import playerColor.PlayerColor;


public class JoinGameService extends Service <JoinGameRequest, JoinGameResponse> {

  /**
   * Handles a join game request.
   * Attempts to add the existing user to the provided game.
   * Returns a JoinGameResponse object (successful if properly added, unsuccessful if exception
   * thrown during adding)
   *
   * @param joinGameRequest joinGame request
   * @return                joinGame response
   */

  @Override
  public JoinGameResponse processHandlerRequest(JoinGameRequest joinGameRequest) {
    JoinGameResponse joinGameResponse;

    try {
      // Tries to get username from auth token
      String currentUsername = authDao.getUsernameFromAuthToken(joinGameRequest.authToken());

      // Tries to get desired game from provided ID
      GameData desiredGame = gameDao.get(joinGameRequest.gameID());
      if (desiredGame == null) {
        throw new DataAccessException(DataAccessException.ErrorMessages.BAD_REQUEST);
      }

      int currGameID = desiredGame.getGameID();
      PlayerColor desiredColor = joinGameRequest.playerColor();

      if (desiredColor == null) {
        gameDao.addObserver(currGameID, currentUsername);
      }
      else {
        gameDao.setPlayer(currGameID, desiredColor, currentUsername);
      }

      joinGameResponse = new JoinGameResponse();
    }
    catch (DataAccessException e) {
      joinGameResponse = new JoinGameResponse(e.getMessage()); // Unsuccessful joinGame
    }

    return joinGameResponse;
  }
}
