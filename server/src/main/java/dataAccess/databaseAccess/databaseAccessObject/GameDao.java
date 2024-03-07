package dataAccess.databaseAccess.databaseAccessObject;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.databaseAccess.DatabaseAccessObject;
import model.GameData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class GameDao extends DatabaseAccessObject<Integer, GameData> {

  private final static String GAME_DATABASE_NAME = "gamedata";


  public GameDao() throws DataAccessException {}

  @Override
  public Integer create(GameData data) throws DataAccessException {
    try (var preparedStatement = connection.prepareStatement("INSERT INTO " + GAME_DATABASE_NAME + " (whiteUsername, blackUsername, gameName, game) VALUES(?, ?, ?, ?)", RETURN_GENERATED_KEYS)) {

      // Default both usernames to null
      preparedStatement.setString(1, null);
      preparedStatement.setString(2, null);

      // Setting white username, if given
      if (data.getWhiteUsername() != null) {
        preparedStatement.setString(1, data.getWhiteUsername());
      }


      // Setting black username, if given
      if (data.getBlackUsername() != null) {
        preparedStatement.setString(2, data.getBlackUsername());
      }

      // Setting game name (necessary)
      preparedStatement.setString(3, data.getGameName());

      // Setting serialized game (if null, initialize)
      if (data.getGame() == null) {
        data.initializeEmptyGame();
      }

      String serializedGame = gson.toJson(data.getGame());
      preparedStatement.setString(4, serializedGame);

      preparedStatement.executeUpdate();

      var resultSet = preparedStatement.getGeneratedKeys();
      int gameID;

      if (resultSet.next()) {
         gameID = resultSet.getInt(1);
      }
      else {
        // Result set is empty
        throw new IllegalStateException();
      }

      return gameID;
    }
    catch (SQLException ex) {
      throw new DataAccessException("Game data couldn't be inserted: " + ex.getMessage());
    }
  }

  @Override
  public GameData get(Integer key) throws DataAccessException {
    //TODO add observers to sql query
    try (var preparedStatement = connection.prepareStatement("SELECT gameID, whiteUsername, blackUsername, gameName, game FROM gamedata WHERE gameID=?")) {
      preparedStatement.setInt(1, key);

      try (ResultSet rs = preparedStatement.executeQuery()) {
        int gameID = rs.getInt(1);
        String whiteUsername = rs.getString(2);
        String blackUsername = rs.getString(3);
        String gameName = rs.getString(4);
        ChessGame game = gson.fromJson(rs.getString(5), ChessGame.class);

        return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
      }
    }
    catch (SQLException ex) {
      throw new DataAccessException("Game data could not be retrieved: " + ex.getMessage());
    }
  }

  @Override
  public Collection<GameData> listData() throws DataAccessException {
    // TODO add observers and actual game to query
    try (var preparedStatement = connection.prepareStatement("SELECT gameID, whiteUsername, blackUsername, gameName FROM " + GAME_DATABASE_NAME)) {
      Collection<GameData> games = new ArrayList<>();


      try (ResultSet rs = preparedStatement.executeQuery()) {
        while (rs.next()) {
          //TODO Simplify common extraction logic into readGameData method

          int gameID = rs.getInt(1);
          String whiteUsername = rs.getString(2);
          String blackUsername = rs.getString(3);
          String gameName = rs.getString(4);
          ChessGame game = gson.fromJson(rs.getString(5), ChessGame.class);

          games.add(new GameData(gameID, whiteUsername, blackUsername, gameName, game));
        }
        return games;
      }
    }
    catch (SQLException ex) {
      throw new DataAccessException("Game data could not be retrieved: " + ex.getMessage());
    }
  }

  @Override
  public void delete(Integer key) throws DataAccessException {

  }

  @Override
  public void clear() throws DataAccessException {
    try {
      // TODO Clean this up
      try (var preparedStatement = connection.prepareStatement("SET foreign_key_checks = 0")) {
        preparedStatement.executeUpdate();
      }
      try (var preparedStatement = connection.prepareStatement("TRUNCATE TABLE observers")) {
        preparedStatement.executeUpdate();
      }
      try (var preparedStatement = connection.prepareStatement("TRUNCATE TABLE " + GAME_DATABASE_NAME)) {
        preparedStatement.executeUpdate();
      }
      try (var preparedStatement = connection.prepareStatement("SET foreign_key_checks = 1")) {
        preparedStatement.executeUpdate();
      }
    }
    catch (SQLException ex) {
      throw new DataAccessException(DataAccessException.ErrorMessages.BAD_REQUEST + ": " + ex.getMessage());
    }
  }
}
