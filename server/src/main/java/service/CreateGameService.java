package service;

import dataAccess.DataAccessException;
import model.GameData;
import service.request.CreateGameRequest;
import service.response.CreateGameResponse;


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

      if (!authTokenExists(createGameRequest.authToken())) {
        throw new DataAccessException(ServiceConstants.ErrorMessages.UNAUTHORIZED);
      }

      int gameID = GAME_DAO.create(newGame);
      createGameResponse = new CreateGameResponse(gameID);
    }
    catch (DataAccessException e) {
      createGameResponse = new CreateGameResponse(e.getMessage());
    }

    return createGameResponse;
  }
}

