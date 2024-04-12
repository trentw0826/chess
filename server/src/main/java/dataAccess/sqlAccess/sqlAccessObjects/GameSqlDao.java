package dataAccess.sqlAccess.sqlAccessObjects;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.dataAccessObject.GameDao;
import dataAccess.sqlAccess.SqlAccessObject;
import model.GameData;
import playerColor.PlayerColor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;


public class GameSqlDao extends SqlAccessObject<Integer, GameData> implements GameDao {

  private static final String GAME_TABLE = "gamedata";
  private static final String SQL_FALSE = "0";


  /**
   * Creates a new game in the game table.
   *
   * @param data  new game data to be inserted
   * @return      the new game's auto-generated key
   * @throws DataAccessException  if SQL error thrown during insertion
   */
  @Override
  public Integer create(GameData data) throws DataAccessException {
    try {
      int gameActiveValue = data.isGameActive() ? 1 : 0;
      return executeUpdate("INSERT INTO " + GAME_TABLE +
                    " (whiteUsername, blackUsername, gameName, game, gameActive) VALUES(?, ?, ?, ?, ?)",
                    data.getWhiteUsername(), data.getBlackUsername(), data.getGameName(),
                    new ChessGame(), gameActiveValue);
    }
    catch (SQLException e) {
      throw new DataAccessException("Game data couldn't be inserted: " + e.getMessage());
    }
  }


  /**
   * Retrieves a game from the game table given a game ID.
   *
   * @param key game ID of desired game
   * @return    stored GameData object associated with the given ID
   * @throws DataAccessException  if SQL error thrown during selection
   */
  @Override
  public GameData get(Integer key) throws DataAccessException {
    if (key == null) {
      throw new DataAccessException(DataAccessException.ErrorMessages.BAD_REQUEST);
    }

    try (var preparedStatement = connection.prepareStatement(
            "SELECT gameID, whiteUsername, blackUsername, gameName, game, gameActive FROM " +
                    GAME_TABLE + " WHERE gameID=?")) {
      preparedStatement.setInt(1, key);
      ResultSet rs = preparedStatement.executeQuery();
      if(rs.next()) {
        return readGameData(rs);
      }
      else {
        throw new DataAccessException(DataAccessException.ErrorMessages.BAD_REQUEST);
      }
    }
    catch (SQLException e) {
      throw new DataAccessException("Game data could not be retrieved: " + e.getMessage());
    }
  }


  /**
   * Lists all data held in the game table.
   *
   * @return  collection of 'GameData' objects from the game table
   * @throws DataAccessException  if SQL error is thrown during selection
   */
  @Override
  public Collection<GameData> list() throws DataAccessException {
    try (var preparedStatement = connection.prepareStatement("SELECT gameID, whiteUsername, blackUsername, gameName, game, gameActive FROM " + GAME_TABLE)) {
      Collection<GameData> games = new ArrayList<>();

      try (ResultSet rs = preparedStatement.executeQuery()) {
        while (rs.next()) {
          games.add(readGameData(rs));
        }
        return games;
      }

    }
    catch (SQLException e) {
      throw new DataAccessException("Game data could not be retrieved: " + e.getMessage());
    }
  }


  /**
   * Drops the game found with the given game ID.
   *
   * @param key game ID
   * @throws DataAccessException  if SQL error thrown during dropping
   */
  @Override
  public void delete(Integer key) throws DataAccessException {
    try (var preparedStatement = connection.prepareStatement("DELETE FROM " + GAME_TABLE + " WHERE gameID=?")) {
      preparedStatement.setInt(1, key);
      int rowsAffected = preparedStatement.executeUpdate();
      if (rowsAffected == 0) {
        throw new DataAccessException(DataAccessException.ErrorMessages.BAD_REQUEST);
      }
    }
    catch (SQLException e) {
      throw new DataAccessException(DataAccessException.ErrorMessages.BAD_REQUEST + ": " + e.getMessage());
    }
  }


  /**
   * Clears all data in the game table.
   *
   * @throws DataAccessException  if SQL error thrown during clearing
   */
  @Override
  public void clear() throws DataAccessException {
    try {
      executeUpdate("SET foreign_key_checks = 0");
      executeUpdate("TRUNCATE TABLE observers");
      executeUpdate("TRUNCATE TABLE " + GAME_TABLE);
      executeUpdate("SET foreign_key_checks = 1");
    }
    catch (SQLException e) {
      throw new DataAccessException(DataAccessException.ErrorMessages.BAD_REQUEST + ": " + e.getMessage());
    }
  }



  /**
   * Add an observer to the game associated with the given game ID;
   *
   * @param gameID            game ID
   * @param observerUsername  observer's username
   * @throws DataAccessException  if SQL error thrown during adding
   */
  @Override
  public void addObserver (int gameID, String observerUsername) throws DataAccessException {
    try {
      executeUpdate(
              "INSERT INTO observers (username, gameID) VALUES (?, ?)",
              observerUsername, gameID
      );
    }
    catch (SQLException e) {
      throw new DataAccessException("Observer could not be added: " + e.getMessage());
    }
  }


  /**
   * Sets the player of a game to have the desired color.
   *
   * @param gameID        game ID
   * @param desiredColor  desired color ("white" for white, "black" for black, or null for observer)
   * @param username      joining player's username
   * @throws DataAccessException  if SQL error thrown during adding
   */
  public void setPlayer(int gameID, PlayerColor desiredColor, String username) throws DataAccessException {
    GameData retrievedData = get(gameID);

    if (desiredColor == PlayerColor.WHITE) {
      String whiteUsername = retrievedData.getWhiteUsername();
      if (whiteUsername != null) {
        throw new DataAccessException(DataAccessException.ErrorMessages.ALREADY_TAKEN);
      }
      setWhiteUser(gameID, username);
    }
    else if (desiredColor == PlayerColor.BLACK) {
      String blackUsername = retrievedData.getBlackUsername();
      if (blackUsername != null) {
        throw new DataAccessException(DataAccessException.ErrorMessages.ALREADY_TAKEN);
      }
      setBlackUser(gameID, username);
    }
    else {
      throw new IllegalArgumentException("Bad color passed to setPlayer");
    }
  }


  private static void setWhiteUser(int gameID, String whiteUsername) throws DataAccessException {
    try {
      String setPlayerStatement = "UPDATE " + GAME_TABLE + " SET whiteUsername=? WHERE gameID=?";
      executeUpdate(setPlayerStatement, whiteUsername, gameID);
    }
    catch (SQLException e) {
      throw new DataAccessException("Unable to set player: " + e.getMessage());
    }
  }


  private static void setBlackUser(int gameID, String blackUsername) throws DataAccessException {
    try {
      String setPlayerStatement = "UPDATE " + GAME_TABLE + " SET blackUsername=? WHERE gameID=?";
      executeUpdate(setPlayerStatement, blackUsername, gameID);
    }
    catch (SQLException e) {
      throw new DataAccessException("Unable to set player: " + e.getMessage());
    }
  }


  @Override
  public void removePlayer(int gameID, String username) throws DataAccessException {
    GameData retrievedData = get(gameID);
    String whiteUsername = retrievedData.getWhiteUsername();
    String blackUsername = retrievedData.getBlackUsername();

    if (username.equals(whiteUsername)) {
      setWhiteUser(gameID, null);
    }
    else if (username.equals(blackUsername)) {
      setBlackUser(gameID, null);
    }
    else {
      throw new DataAccessException(String.format("User '%s' is not a part of game id:%s", username, gameID));
    }
  }


  @Override
  public void removeObserver(int gameID, String username) throws DataAccessException {
    try {
      String setPlayerStatement = "DELETE FROM observers WHERE gameID=? AND username=?";
      executeUpdate(setPlayerStatement, gameID, username);
    }
    catch (SQLException e) {
      throw new DataAccessException("Unable to remove observer: " + e.getMessage());
    }
  }


  /**
   * Sets the gameActive column at the given index to 0
   *
   * @param gameID  desired game index
   * @throws DataAccessException  if SQL error thrown during execution
   */
  @Override
  public void gameOver(int gameID) throws DataAccessException {
    get(gameID);
    String setGameOverStatement = "UPDATE " + GAME_TABLE + " SET gameActive=? WHERE gameID=?";
    try {
      executeUpdate(setGameOverStatement, SQL_FALSE, gameID);
      transitionPlayersToObservers(gameID);
    }
    catch (SQLException e) {
      throw new DataAccessException(String.format("Unable to deactivate game with id '%d", gameID));
    }
  }


  /**
   * Adds all active players as observers to the current game.
   *
   * @param gameID  desired gameID
   * @throws DataAccessException  if SQL error thrown during access
   */
  private void transitionPlayersToObservers(int gameID) throws DataAccessException {
    GameData retrievedData = get(gameID);
    String whiteUsername = retrievedData.getWhiteUsername();
    String blackUsername = retrievedData.getBlackUsername();

    if (whiteUsername != null) {
      addObserver(gameID, whiteUsername);
    }
    if (blackUsername != null) {
      addObserver(gameID, blackUsername);
    }
  }

  @Override
  public boolean isGameActive(int gameID) throws DataAccessException {
    return get(gameID).isGameActive();
  }


  @Override
  public boolean usernameIsPlaying(int gameID, String username) throws DataAccessException {
    GameData retrievedData = get(gameID);
    String whiteUsername = retrievedData.getWhiteUsername();
    String blackUsername = retrievedData.getBlackUsername();

    return (username.equals(whiteUsername) || username.equals(blackUsername));
  }


  @Override
  public boolean usernameIsObserving(int gameID, String username) throws DataAccessException {
    String setObserverCheckStatement = "SELECT id FROM observers WHERE username=? AND gameID=?";

    try (var preparedStatement = connection.prepareStatement(setObserverCheckStatement)) {
      preparedStatement.setString(1, username);
      preparedStatement.setInt(2, gameID);
      ResultSet rs = preparedStatement.executeQuery();
      return rs.next();
    }
    catch (SQLException e) {
      throw new DataAccessException(String.format("Unable to check if '%s' is observing game id:%d", username, gameID));
    }
  }


  /**
   * Helper function that takes in the result set representing a chess game from a SELECT query
   * and converts it into a GameData object.
   *
   * @param rs  result set to be read
   * @return    GameData object represented by the given result set
   * @throws SQLException if sql error is thrown during reading
   */
  private GameData readGameData(ResultSet rs) throws SQLException {
    try {
      int gameID = rs.getInt(1);
      String whiteUsername = rs.getString(2);
      String blackUsername = rs.getString(3);
      String gameName = rs.getString(4);
      String chessGameJson = rs.getString(5);
      ChessGame game = gson.fromJson(chessGameJson, ChessGame.class);
      boolean gameActive = rs.getBoolean(6);

      return new GameData(gameID, whiteUsername, blackUsername, gameName, game, gameActive);
    }
    catch (SQLException e) {
      throw new IllegalArgumentException("Bad result set passed to readGameData: " + e.getMessage());
    }
  }
}
