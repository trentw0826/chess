package dataAccess.dataAccessObject;

import dataAccess.DataAccessException;
import dataAccess.DataAccessObject;
import model.GameData;
import playerColor.PlayerColor;

public interface GameDao extends DataAccessObject<Integer, GameData> {

  /**
   * Add an observer to the game associated with the given game ID;
   *
   * @param gameID            game ID
   * @param observerUsername  observer's username
   * @throws DataAccessException  if SQL error thrown during adding
   */
  void addObserver (final int gameID, final String observerUsername) throws DataAccessException;


  /**
   * Sets the player of a game to have the desired color.
   *
   * @param gameID    game ID
   * @param color     desired color ("white" for white, "black" for black, or null for observer)
   * @param username  joining player's username
   * @throws DataAccessException  if SQL error thrown during adding
   */
  void setPlayer(final int gameID, final PlayerColor color, final String username) throws DataAccessException;
}
