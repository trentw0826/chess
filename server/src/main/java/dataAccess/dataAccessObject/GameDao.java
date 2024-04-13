package dataAccess.dataAccessObject;

import chess.ChessMove;
import chess.InvalidMoveException;
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
   * @throws DataAccessException  if error thrown during adding
   */
  void addObserver (final int gameID, final String observerUsername) throws DataAccessException;


  /**
   * Sets the player of a game to have the desired color.
   *
   * @param gameID    game ID
   * @param color     desired color ("white" for white, "black" for black, or null for observer)
   * @param username  joining player's username
   * @throws DataAccessException  if error thrown during adding
   */
  void setPlayer(final int gameID, final PlayerColor color, final String username) throws DataAccessException;

  /**
   * Sets the gameActive column at the given index to 0
   *
   * @param gameID  desired game index
   */
  void gameOver(int gameID) throws DataAccessException;


  /**
   * Returns if a given username is playing as white or black.
   *
   * @param gameID    desired game ID
   * @param username  username to be checked
   * @return          if 'username' matches either white or black player
   * @throws DataAccessException  if error thrown during checking
   */
  boolean usernameIsPlaying(int gameID, String username) throws DataAccessException;


  /**
   * Returns if a game is currently active.
   *
   * @param gameID  desired game ID
   * @return        if game associated with 'gameID' is active
   * @throws DataAccessException  if error thrown during retrieval
   */
  boolean isGameActive(int gameID) throws DataAccessException;


  /**
   * Returns if the given user is observing the given game.
   *
   * @param gameID    desired game ID
   * @param username  desired username
   * @return          if 'username' is observing game with id 'gameID'
   * @throws DataAccessException  if error thrown during access
   */
  boolean usernameIsObserving(int gameID, String username) throws DataAccessException;

  /**
   * Removes a player's reservation in a game, setting that position to null.
   *
   * @param gameID    desired game ID
   * @param username  username of player to be removed
   * @throws DataAccessException  if error thrown during removal
   */
  void removePlayer(int gameID, String username) throws DataAccessException;

  /**
   * Removes an observer from the given gameID
   *
   * @param gameID    desired game ID0
   * @param username  username of observer to be removed
   * @throws DataAccessException  if error thrown during removal
   */
  void removeObserver(int gameID, String username) throws DataAccessException;


  /**
   * Update a game by making a move in the database.
   *
   * @param gameID  game ID to be updated
   * @param move    chess move
   * @throws InvalidMoveException if chess move is invalid
   * @throws DataAccessException  if error thrown during update
   */
  void makeMove(int gameID, ChessMove move) throws InvalidMoveException, DataAccessException;
}
