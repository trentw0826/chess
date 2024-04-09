package service;

import dataAccess.DataAccessException;
import model.GameData;
import request.httpRequests.CreateGameRequest;
import response.CreateGameResponse;


public class CreateGameService extends Service <CreateGameRequest, CreateGameResponse> {

  /**
   * Handles a create game request.
   * Returns a negative CreateGameResponse if the data couldn't be created and inserted into the database,
   * otherwise returns a positive one.
   *
   * @param createGameRequest create game request
   * @return                  create game response
   */
  @Override
  public CreateGameResponse processHandlerRequest(CreateGameRequest createGameRequest) {
    CreateGameResponse createGameResponse;

    GameData newGame = new GameData(createGameRequest.gameName());

    try {
      if (invalidAuthToken(createGameRequest.authToken())) {
        throw new DataAccessException(DataAccessException.ErrorMessages.UNAUTHORIZED);
      }

      int gameID = gameDao.create(newGame);
      createGameResponse = new CreateGameResponse(gameID);
    }
    catch (DataAccessException e) {
      createGameResponse = new CreateGameResponse(e.getMessage());
    }

    return createGameResponse;
  }
}

