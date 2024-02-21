package dataAccess.memoryAccess;

import dataAccess.DataAccessException;
import model.GameData;

/**
 * Defines a data access object that accesses and modifies locally stored game data
 */
public class MemoryGameDAO extends MemoryDAO<Integer, GameData> {

  /**
   * Returns the key of a GameData object.
   *
   * @param data  game data model
   * @return      key of 'data' (gameID)
   */
  @Override
  Integer generateKey(GameData data) {
    return data.gameID();
  }

  void updateGame() throws DataAccessException {
    /*
    TODO Implement (should it take a string, chess game object, or gamedata object?)
     */
  }
}
