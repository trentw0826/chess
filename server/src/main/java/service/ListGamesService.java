package service;

import dataAccess.DataAccessException;
import service.request.ListGamesRequest;
import service.response.ListGamesResponse;


public class ListGamesService extends Service <ListGamesRequest, ListGamesResponse> {

  /**
   * Takes a listGames request and retrieves a list of all active games in the database. Returns
   * that list of games in the form of a ListGamesRequest object
   *
   * @param listGamesRequest  list games request
   * @return                  list games response
   */
  @Override
  public ListGamesResponse processHandlerRequest(ListGamesRequest listGamesRequest) {
    ListGamesResponse listGamesResponse;

    try {
      if (invalidAuthToken(listGamesRequest.authToken())) {
        throw new DataAccessException(DataAccessException.ErrorMessages.UNAUTHORIZED);
      }

      listGamesResponse = new ListGamesResponse(GAME_DAO.list());
    }
    catch (DataAccessException e) {
      listGamesResponse = new ListGamesResponse(e.getMessage()); // Unsuccessful listGames
    }

    return listGamesResponse;
  }
}
