package dataAccess.databaseAccess.databaseAccessObject;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.databaseAccess.DatabaseAccessObject;
import model.GameData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;


public class GameDao extends DatabaseAccessObject<Integer, GameData> {

  private static final String GAME_DATABASE_NAME = "gamedata";


  public GameDao() throws DataAccessException {
    super();
  }


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
      return executeUpdate("INSERT INTO " + GAME_DATABASE_NAME +
                    " (whiteUsername, blackUsername, gameName, game) VALUES(?, ?, ?, ?)",
                    data.getWhiteUsername(), data.getBlackUsername(), data.getGameName(), GameData.getEmptyGame());
    }
    catch (SQLException ex) {
      throw new DataAccessException("Game data couldn't be inserted: " + ex.getMessage());
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
    try (var preparedStatement = connection.prepareStatement("SELECT gameID, whiteUsername, blackUsername, gameName, game FROM gamedata WHERE gameID=?")) {
      preparedStatement.setInt(1, key);

      try (ResultSet rs = preparedStatement.executeQuery()) {
        if (rs.next()) {
          int gameID = rs.getInt(1);
          String whiteUsername = rs.getString(2);
          String blackUsername = rs.getString(3);
          String gameName = rs.getString(4);
          ChessGame game = gson.fromJson(rs.getString(5), ChessGame.class);

          return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
        }
        else {
          throw new IllegalStateException("Result set with no rows detected");
        }
      }
    }
    catch (SQLException ex) {
      throw new DataAccessException("Game data could not be retrieved: " + ex.getMessage());
    }
  }


  /**
   * Lists all data held in the game table.
   *
   * @return  collection of 'GameData' objects from the game table
   * @throws DataAccessException  if SQL error is thrown during selection
   */
  @Override
  public Collection<GameData> listData() throws DataAccessException {
    // TODO add observers and actual game to query
    try (var preparedStatement = connection.prepareStatement("SELECT gameID, whiteUsername, blackUsername, gameName, game FROM " + GAME_DATABASE_NAME)) {
      Collection<GameData> games = new ArrayList<>();

      try (ResultSet rs = preparedStatement.executeQuery()) {
        while (rs.next()) {
          games.add(readGameData(rs));
        }
        return games;
      }

    }
    catch (SQLException ex) {
      throw new DataAccessException("Game data could not be retrieved: " + ex.getMessage());
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
      executeUpdate("TRUNCATE TABLE " + GAME_DATABASE_NAME);
      executeUpdate("SET foreign_key_checks = 1");
    }
    catch (SQLException ex) {
      throw new DataAccessException(DataAccessException.ErrorMessages.BAD_REQUEST + ": " + ex.getMessage());
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
    int gameID = rs.getInt(1);
    String whiteUsername = rs.getString(2);
    String blackUsername = rs.getString(3);
    String gameName = rs.getString(4);
    String chessGameJson = rs.getString(5);
    ChessGame game = gson.fromJson(chessGameJson, ChessGame.class);

    return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
  }
}
