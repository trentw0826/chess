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

      String desiredGamesWhitePlayer = desiredGame.getWhiteUsername();
      String desiredGamesBlackPlayer = desiredGame.getBlackUsername();
      String desiredColor = joinGameRequest.playerColor();

      if (desiredColor == null) {
        // Add the user as an observer
        desiredGame.addObserver(currentUsername);
      }
      else if (desiredColor.equalsIgnoreCase("white")) {
        if (desiredGamesWhitePlayer == null) {
          // Player wants to be added as white player, which is available
          desiredGame.setWhiteUsername(currentUsername);
        }
        else {
          // Player wants to be added as white player, but it is unavailable
          throw new DataAccessException(ServiceConstants.ERROR_MESSAGES.ALREADY_TAKEN);
        }
      }
      else if (desiredColor.equalsIgnoreCase("black")) {
        if (desiredGamesBlackPlayer == null) {
          // Player wants to be added as black player, which is available
          desiredGame.setBlackUsername(currentUsername);
        }
        else {
          // Player wants to be added as black player, but it is unavailable
          throw new DataAccessException(ServiceConstants.ERROR_MESSAGES.ALREADY_TAKEN);
        }
      }
      else {
        // player's desired player color could not be recognized as white, black, or observer
        throw new DataAccessException(ServiceConstants.ERROR_MESSAGES.BAD_REQUEST);
      }

      joinGameResponse = new JoinGameResponse();
    }
    catch (DataAccessException e) {
      joinGameResponse = new JoinGameResponse(e.getMessage()); // Unsuccessful joinGame
    }

    return joinGameResponse;
  }
}
