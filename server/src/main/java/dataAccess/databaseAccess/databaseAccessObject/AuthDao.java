package dataAccess.databaseAccess.databaseAccessObject;

import dataAccess.DataAccessException;
import dataAccess.databaseAccess.DatabaseAccessObject;
import model.AuthData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;


public class AuthDao extends DatabaseAccessObject<String, AuthData> {

  private static final String AUTH_TABLE = "authdata";


  /**
   * Inserts a new AuthData entry into the auth table.
   * 
   * @param data  new AuthData to be inserted
   * @return      auth token of inserted data
   * @throws DataAccessException  if SQL error thrown during insertion
   */
  @Override
  public String create(AuthData data) throws DataAccessException {

    try {
      executeUpdate("INSERT INTO " + AUTH_TABLE +
              " (authToken, username) VALUES(?, ?)", data.authToken(), data.username());
      return data.authToken();
    }
    catch (SQLException e) {
      throw new DataAccessException(DataAccessException.ErrorMessages.BAD_REQUEST + ": " + e.getMessage());
    }
  }


  /**
   * Retrieves an AuthData object from the auth table.
   * 
   * @param key auth token of desired AuthData entry
   * @return    AuthData object associated with given auth token
   * @throws DataAccessException  if SQL error thrown during retrieval
   */
  @Override
  public AuthData get(String key) throws DataAccessException {
    try (var preparedStatement = connection.prepareStatement(
            "SELECT * FROM " + AUTH_TABLE + " WHERE authToken=?"
    )) {
      preparedStatement.setString(1, key);
      ResultSet rs = preparedStatement.executeQuery();
      rs.next();
      try {
        return readAuthData(rs);
      }
      catch (IllegalArgumentException e) {
        throw new DataAccessException(DataAccessException.ErrorMessages.BAD_REQUEST + ": auth doesn't exist");
      }
    }
    catch (SQLException e) {
      throw new DataAccessException("Auth data could not be retrieved: " + e.getMessage());
    }
  }


  /**
   * Lists all the data currently held in the auth table.
   * 
   * @return  collection of AuthData objects representing all auth entries
   */
  @Override
  public Collection<AuthData> listData() throws DataAccessException {
    try (var preparedStatement = connection.prepareStatement(
            "SELECT * FROM " + AUTH_TABLE
    )) {
      Collection<AuthData> auths = new ArrayList<>();

      try (ResultSet rs = preparedStatement.executeQuery()) {
        while (rs.next()) {
          auths.add(readAuthData(rs));
        }
        return auths;
      }
    }
    catch (SQLException e) {
      throw new DataAccessException("Game data could not be retrieved: " + e.getMessage());
    }
  }


  /**
   * Drops an auth data entry from the auth table.
   * 
   * @param key auth token of the to-be-dropped auth entry
   * @throws DataAccessException  if SQL error thrown during dropping
   */
  @Override
  public void delete(String key) throws DataAccessException {
    try (var preparedStatement = connection.prepareStatement("DELETE FROM " + AUTH_TABLE + " WHERE authToken=?")) {
      preparedStatement.setString(1, key);
      int rowsAffected = preparedStatement.executeUpdate();
      if (rowsAffected == 0) {
        throw new DataAccessException(DataAccessException.ErrorMessages.UNAUTHORIZED);
      }
    }
    catch (SQLException e) {
      throw new DataAccessException(DataAccessException.ErrorMessages.BAD_REQUEST + ": " + e.getMessage());
    }
  }


  /**
   * Truncates the auth table, deleting all the currently held data.
   *
   * @throws DataAccessException  if SQL error thrown during truncation
   */
  @Override
  public void clear() throws DataAccessException {
    try {
      executeUpdate("TRUNCATE TABLE " + AUTH_TABLE);
    }
    catch (SQLException e) {
      throw new DataAccessException(DataAccessException.ErrorMessages.BAD_REQUEST + ": " + e.getMessage());
    }
  }


  /**
   * Helper function that takes in the result set representing auth data from a SELECT query
   * and converts it into a AuthData object.
   *
   * @param rs  result set to be read
   * @return    AuthData object represented by the given result set
   * @throws SQLException if sql error is thrown during reading
   */
  private AuthData readAuthData(ResultSet rs) throws SQLException {
    try {
      String authToken = rs.getString(1);
      String username = rs.getString(2);

      return new AuthData(authToken, username);
    }
    catch (SQLException e) {
      throw new IllegalArgumentException("Bad result set passed to readAuthData: " + e.getMessage());
    }
  }


  //TODO Extract common extension here upon reorganization of DAO structure
  public String getUsernameFromAuthToken(String authToken) throws DataAccessException {
    try {
      return get(authToken).username();
    }
    catch (DataAccessException e) {
      throw new DataAccessException(DataAccessException.ErrorMessages.UNAUTHORIZED);
    }
  }
}
