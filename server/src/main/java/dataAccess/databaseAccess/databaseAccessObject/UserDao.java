package dataAccess.databaseAccess.databaseAccessObject;

import dataAccess.DataAccessException;
import dataAccess.databaseAccess.DatabaseAccessObject;
import model.UserData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;


public class UserDao extends DatabaseAccessObject<String, UserData> {

  private static final String USER_DATABASE_NAME = "userdata";

  public UserDao() throws DataAccessException {
    super();
  }


  /**
   * Inserts a new user data entry in the user table.
   *
   * @param data user data to be inserted
   * @return    the username of the inserted data
   * @throws DataAccessException  if SQL error thrown during insertion
   */
  @Override
  public String create(UserData data) throws DataAccessException {
    try {
      executeUpdate("INSERT INTO " + USER_DATABASE_NAME +
              " (username, password, email) VALUES(?, ?, ?)",
              data.username(), data.password(), data.email());

      return data.username();
    }
    catch (SQLException e) {
      throw new DataAccessException(DataAccessException.ErrorMessages.BAD_REQUEST + ": " + e.getMessage());
    }
  }


  /**
   * Selects a user data entry at the provided username from the user table.
   *
   * @param key username of desired user data entry
   * @return    user data associated with given username
   * @throws DataAccessException  if SQL error thrown during selection
   */
  @Override
  public UserData get(String key) throws DataAccessException {
    try (var preparedStatement = connection.prepareStatement(
            "SELECT * FROM " + USER_DATABASE_NAME + " WHERE username=?")) {
      preparedStatement.setString(1, key);
      ResultSet rs = preparedStatement.executeQuery();
      return readUserData(rs);
    }
    catch (SQLException e) {
      throw new DataAccessException("User data could not be retrieved: " + e.getMessage());
    }
  }


  /**
   * List all the user data entries found in the user table.
   *
   * @return  collection of user data objects representing all the user data entries
   */
  @Override
  public Collection<UserData> listData() {
    return Collections.emptyList();
  }


  /**
   * Drops a user data entry at the given username key from the user table.
   *
   * @param key username of user data to be dropped
   * @throws DataAccessException  if SQL error thrown during dropping
   */
  @Override
  public void delete(String key) throws DataAccessException {

  }


  /**
   * Truncates the user data table.
   *
   * @throws DataAccessException  if SQL error thrown during truncation
   */
  @Override
  public void clear() throws DataAccessException {
    try {
      executeUpdate("TRUNCATE TABLE " + USER_DATABASE_NAME);
    }
    catch (SQLException e) {
      throw new DataAccessException(DataAccessException.ErrorMessages.BAD_REQUEST + ": " + e.getMessage());
    }
  }

  public boolean attemptPassword(String username, String password) {
    return false;
  }

  private UserData readUserData(ResultSet rs) throws SQLException {
    if (rs.next()) {
      String username = rs.getString(1);
      String password = rs.getString(2);
      String email = rs.getString(3);

      return new UserData(username, password, email);
    }
    else {
      throw new IllegalArgumentException("Empty result set passed to readUserData");
    }
  }
}
