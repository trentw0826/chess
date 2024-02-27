package service;

import dataAccess.DataAccessException;
import model.GameData;
import service.request.CreateGameRequest;
import service.response.CreateGameResponse;


public class CreateGameService extends Service <CreateGameRequest, CreateGameResponse> {

  /**
   * Takes a createGame request, converts it into a GameData object,
   * and attempts to insert that game data into the game database.
   * Returns a CreateGameResponse object (successful if properly added, unsuccessful if exception
   * thrown during adding)
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
        throw new DataAccessException(ServiceConstants.ERROR_MESSAGES.UNAUTHORIZED);
      }

      if (!newGame.hasGameName()) {
        throw new DataAccessException(ServiceConstants.ERROR_MESSAGES.BAD_REQUEST);
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
