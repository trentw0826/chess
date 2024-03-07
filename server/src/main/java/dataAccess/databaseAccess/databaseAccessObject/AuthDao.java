package dataAccess.databaseAccess.databaseAccessObject;

import dataAccess.DataAccessException;
import dataAccess.databaseAccess.DatabaseAccessObject;
import model.AuthData;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;


public class AuthDao extends DatabaseAccessObject<String, AuthData> {

  private static final String AUTH_DATABASE_NAME = "authdata";

  public AuthDao() throws DataAccessException {
    super();
  }


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
      executeUpdate("INSERT INTO " + AUTH_DATABASE_NAME +
              " (authToken, username) VALUES(?, ?)", data.authToken(), data.username());
      return data.authToken();
    }
    catch (SQLException ex) {
      throw new DataAccessException(DataAccessException.ErrorMessages.BAD_REQUEST + ": " + ex.getMessage());
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
    try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT authToken, username FROM " + AUTH_DATABASE_NAME + " WHERE authtoken=?")) {
      preparedStatement.setString(1, key);
      ResultSet resultSet = preparedStatement.executeQuery();

      return readAuthData(resultSet);
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
  public Collection<AuthData> listData() {
    return Collections.emptyList();
  }


  /**
   * Drops an auth data entry from the auth table.
   * 
   * @param key auth token of the to-be-dropped auth entry
   * @throws DataAccessException  if SQL error thrown during dropping
   */
  @Override
  public void delete(String key) throws DataAccessException {

  }


  /**
   * Truncates the auth table, deleting all the currently held data.
   *
   * @throws DataAccessException  if SQL error thrown during truncation
   */
  @Override
  public void clear() throws DataAccessException {
    try {
      executeUpdate("TRUNCATE TABLE " + AUTH_DATABASE_NAME);
    }
    catch (SQLException ex) {
      throw new DataAccessException(DataAccessException.ErrorMessages.BAD_REQUEST + ": " + ex.getMessage());
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
    if (rs.next()) {
      String authToken = rs.getString(1);
      String username = rs.getString(2);
      return new AuthData(authToken, username);
    }

    throw new IllegalArgumentException("Empty result set passed to readAuthData");
  }


  //TODO Extract common definition between authdao and authmao?
  public String getUsernameFromAuthToken(String authToken) throws DataAccessException {
    return get(authToken).username();
  }
}
