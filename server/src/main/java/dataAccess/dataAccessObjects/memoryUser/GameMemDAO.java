package dataAccess.dataAccessObjects.memoryUser;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.dataAccessObjects.GameDAO;
import model.GameData;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class GameMemDAO implements GameDAO {

  private Map<Integer, GameData> gameDataCollection = Collections.emptyMap();

  @Override
  public void clearData() {
    gameDataCollection.clear();
  }

  @Override
  public void createGame(GameData gameData) throws DataAccessException {
    GameData replacedUser = gameDataCollection.put(gameData.gameID(), gameData);
    if (replacedUser == null) {
      throw new DataAccessException("Game with gameID: '" + gameData.gameID() + "' already exists");
    }
  }

  @Override
  public Collection<GameData> listGames() {
    return gameDataCollection.values();
  }

  @Override
  public GameData getGame(int gameID) throws DataAccessException {
    GameData user = gameDataCollection.get(gameID);
    if (user != null) {
      return user;
    }
    else {
      throw new DataAccessException("gameID '" + gameID + "' was not found");
    }
  }

  // TODO: Simplify by making use of existing logic in getGame
  @Override
  public void updateGame(int gameID, GameData game) throws DataAccessException {
    GameData existingGame = gameDataCollection.put(gameID, game);
    if (existingGame != null) {
      throw new DataAccessException("game with gameID '" + gameID + "' already exists");
    }
  }
}
