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
      //TODO Should game be initialized as a new ChessGame or null?
      return executeUpdate("INSERT INTO " + GAME_TABLE +
                    " (whiteUsername, blackUsername, gameName, game) VALUES(?, ?, ?, ?)",
                    data.getWhiteUsername(), data.getBlackUsername(), data.getGameName(), null);
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
    //TODO add observers to sql query
    //TODO update to 'SELECT *'
    if (key == null) {
      throw new DataAccessException(DataAccessException.ErrorMessages.BAD_REQUEST);
    }

    try (var preparedStatement = connection.prepareStatement(
            "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM " +
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
    // TODO add observers and actual game to query
    try (var preparedStatement = connection.prepareStatement("SELECT gameID, whiteUsername, blackUsername, gameName, game FROM " + GAME_TABLE)) {
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

      return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
    }
    catch (SQLException e) {
      throw new IllegalArgumentException("Bad result set passed to readGameData: " + e.getMessage());
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
   * @param gameID    game ID
   * @param color     desired color ("white" for white, "black" for black, or null for observer)
   * @param username  joining player's username
   * @throws DataAccessException  if SQL error thrown during adding
   */
  public void setPlayer(int gameID, PlayerColor color, String username) throws DataAccessException {
    GameData retrievedData = get(gameID);
    String setPlayerStatement;

    if (color == PlayerColor.WHITE) {
      if (retrievedData.getWhiteUsername() != null) {
        throw new DataAccessException(DataAccessException.ErrorMessages.ALREADY_TAKEN);
      }
      else {
        setPlayerStatement = "UPDATE " + GAME_TABLE + " SET whiteUsername=? WHERE gameID=?";
      }
    }
    else if (color == PlayerColor.BLACK) {
      if (retrievedData.getBlackUsername() != null) {
        throw new DataAccessException(DataAccessException.ErrorMessages.ALREADY_TAKEN);
      }
      else {
        setPlayerStatement = "UPDATE " + GAME_TABLE + " SET blackUsername=? WHERE gameID=?";
      }
    }
    else {
      throw new IllegalArgumentException("Bad color passed to setPlayer");
    }

    try {
      executeUpdate(setPlayerStatement, username, gameID);
    }

    catch (SQLException e) {
      throw new DataAccessException("Unable to set player: " + e.getMessage());
    }
  }
}
