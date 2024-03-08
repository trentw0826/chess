package service;

import dataAccess.DataAccessException;
import model.GameData;
import service.request.JoinGameRequest;
import service.response.JoinGameResponse;


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
      String currentUsername = AUTH_DAO.getUsernameFromAuthToken(joinGameRequest.authToken());

      // Tries to get desired game from provided ID
      GameData desiredGame = GAME_DAO.get(joinGameRequest.gameID());
      if (desiredGame == null) {
        throw new DataAccessException(DataAccessException.ErrorMessages.BAD_REQUEST);
      }

      int currGameID = desiredGame.getGameID();
      String desiredColor = joinGameRequest.playerColor();

      if (desiredColor == null) {
        GAME_DAO.addObserver(currGameID, currentUsername);
      }
      else if (desiredColor.equalsIgnoreCase("white")) {
        GAME_DAO.setPlayer(currGameID, "white", currentUsername);
      }
      else if (desiredColor.equalsIgnoreCase("black")) {
        GAME_DAO.setPlayer(currGameID, "black", currentUsername);
      }
      else {
        throw new IllegalArgumentException("Bad color passed to join game service");
      }

      joinGameResponse = new JoinGameResponse();
    }
    catch (DataAccessException e) {
      joinGameResponse = new JoinGameResponse(e.getMessage()); // Unsuccessful joinGame
    }

    return joinGameResponse;
  }
}
