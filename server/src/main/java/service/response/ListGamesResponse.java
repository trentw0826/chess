package service.response;

import model.GameData;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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
