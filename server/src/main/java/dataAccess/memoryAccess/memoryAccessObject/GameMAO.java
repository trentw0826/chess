package dataAccess.memoryAccess.memoryAccessObject;

import dataAccess.DataAccessException;
import dataAccess.memoryAccess.MemoryAccessObject;
import model.GameData;
import service.ServiceConstants;

/**
 * Defines a data access object that accesses and modifies locally stored game data
 */
public class GameMAO extends MemoryAccessObject<Integer, GameData> {

  private static int numGames;

  /**
   * Returns the key of a GameData object.
   *
   * @param data  game data model
   * @return      key of 'data' (gameID)
   */
  @Override
  public Integer generateKey(GameData data) {
    return data.gameID();
  }

  @Override
  public Integer create(GameData data) throws DataAccessException {
    int gameID = ++numGames;

    if (dataExists(gameID)) {
      throw new DataAccessException(ServiceConstants.ERROR_MESSAGES.ALREADY_TAKEN);
    }
    else {
      localData.put(gameID, data);
      return gameID;
    }
  }

  void updateGame() throws DataAccessException {
    /*
    TODO Implement (should it take a string, chess game object, or gamedata object?)
     */
  }
}
