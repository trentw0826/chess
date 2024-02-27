package service.response;

import model.GameData;

import java.util.Collection;
import java.util.HashSet;

public class ListGamesResponse extends ServiceResponse {
  private Collection<GameData> games = new HashSet<>();

  // Successful
  public ListGamesResponse(Collection<GameData> games) {
    super();
    this.games = games;
  }

  // Unsuccessful
  public ListGamesResponse(String errorMessage) {
    super(errorMessage);
  }

  public Collection<GameData> getGames() {
    return games;
  }
}
