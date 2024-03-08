package dataAccess.memoryAccess.memoryAccessObject;


import dataAccess.DataAccessException;
import dataAccess.GameDao;
import dataAccess.memoryAccess.MemoryAccessObject;
import model.GameData;

import java.util.Map;

/**
 * Defines a data access object that accesses and modifies locally stored game data
 */
public class GameMao extends MemoryAccessObject<Integer, GameData> implements GameDao {

  private static int numGames;

  /**
   * Creates a new game in the game database.
   *
   * @param data  game to be created
   * @return      the newly created game ID
   * @throws DataAccessException  if game already exists
   */
  @Override
  public Integer create(GameData data) throws DataAccessException {
    int gameID = (data.getGameID() == null) ? getNextID() : data.getGameID();

    if (dataExists(gameID)) {
      // a game under given gameID already exists
      throw new DataAccessException(DataAccessException.ErrorMessages.ALREADY_TAKEN);
    }
    else if (nameAlreadyExists(data.getGameName())) {
      // a game under given name already exists
      throw new DataAccessException(DataAccessException.ErrorMessages.ALREADY_TAKEN);
    }
    else {
      // Successful creation
      data.setGameID(gameID);
      localData.put(gameID, data);
      incrementNumGames();
      return gameID;
    }
  }


  /**
   * @return  the next potential game ID (the total number of games + 1)
   */
  private Integer getNextID() {
    return numGames + 1;
  }


  /**
   * Increments the total number of games by one
   */
  private static void incrementNumGames() {
    ++numGames;
  }


  /**
   * Checks if a game with the given name already exists in the game data.
   * Iterates over the game data's entry set, checking each game name.
   *
   * @param gameName  game name to be checked
   * @return          if game name is already found
   */
  protected boolean nameAlreadyExists(String gameName) {
    for (Map.Entry<Integer, GameData> entry : localData.entrySet()) {
      if (entry.getValue().getGameName().equals(gameName)) {
        return true;
      }
    }
    return false;
  }


  /**
   * Add an observer to the game associated with the given game ID;
   *
   * @param gameID            game ID
   * @param observerUsername  observer's username
   * @throws DataAccessException  if SQL error thrown during adding
   */
  @Override
  public void addObserver(int gameID, String observerUsername) {
    GameData retrievedGame = localData.get(gameID);
    retrievedGame.addObserver(observerUsername);
    localData.put(gameID, retrievedGame);
  }

  /**
   * Sets the player of a game to have the desired color.
   *
   * @param gameID    game ID
   * @param color     desired color ("white" for white, "black" for black, or null for observer)
   * @param username  joining player's username
   * @throws DataAccessException  if SQL error thrown during adding
   */
  @Override
  public void setPlayer(int gameID, String color, String username) throws DataAccessException {
    GameData retrievedGame = localData.get(gameID);

    String currWhiteUser = retrievedGame.getWhiteUsername();
    String currBlackUser = retrievedGame.getBlackUsername();

    if (color == null) {
      addObserver(gameID, username);
    }
    else if (color.equalsIgnoreCase("white")) {
      if (currWhiteUser != null) {
        retrievedGame.setWhiteUsername(username);
      }
      else {
        throw new DataAccessException(DataAccessException.ErrorMessages.ALREADY_TAKEN.message());
      }
    }
    else if (color.equalsIgnoreCase("black")) {
      if (currBlackUser != null) {
        retrievedGame.setBlackUsername(username);
      }
      else {
        throw new DataAccessException(DataAccessException.ErrorMessages.ALREADY_TAKEN.message());
      }
    }
    else {
      // Color couldn't be recognized as white, black, or an observer
      throw new DataAccessException(DataAccessException.ErrorMessages.BAD_REQUEST.message());
    }

    localData.put(gameID, retrievedGame);
  }

}
