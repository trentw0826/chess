package dataAccess.memoryAccess.memoryAccessObject;

import dataAccess.DataAccessException;
import dataAccess.memoryAccess.MemoryAccessObject;
import model.GameData;

/**
 * Defines a data access object that accesses and modifies locally stored game data
 */
public class GameMAO extends MemoryAccessObject<Integer, GameData> {

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

  void updateGame() throws DataAccessException {
    /*
    TODO Implement (should it take a string, chess game object, or gamedata object?)
     */
  }
}
