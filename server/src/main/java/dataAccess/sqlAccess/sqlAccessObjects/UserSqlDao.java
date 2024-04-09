package dataAccess.sqlAccess.sqlAccessObjects;

import dataAccess.DataAccessException;
import dataAccess.dataAccessObject.UserDao;
import dataAccess.sqlAccess.SqlAccessObject;
import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;


public class UserSqlDao extends SqlAccessObject<String, UserData> implements UserDao {

  private static final String USER_TABLE = "userdata";
  BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();


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
      executeUpdate(
              "INSERT INTO " + USER_TABLE + " (username, password, email) VALUES(?, ?, ?)",
              data.username(), hashPassword(data.password()), data.email()
      );

      return data.username();
    }
    catch (SQLException e) {
      throw new DataAccessException(DataAccessException.ErrorMessages.ALREADY_TAKEN.message() + ": " + e.getMessage());
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
            "SELECT * FROM " + USER_TABLE + " WHERE username=?"
    )) {
      preparedStatement.setString(1, key);
      ResultSet rs = preparedStatement.executeQuery();
      rs.next();
      try {
        return readUserData(rs);
      }
      catch (IllegalArgumentException e) {
        throw new DataAccessException(DataAccessException.ErrorMessages.BAD_REQUEST + ": user doesn't exist");
      }
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
  public Collection<UserData> list() throws DataAccessException {
    try (var preparedStatement = connection.prepareStatement(
            "SELECT * FROM " + USER_TABLE
    )) {
      Collection<UserData> users = new ArrayList<>();

      try (ResultSet rs = preparedStatement.executeQuery()) {
        while (rs.next()) {
          users.add(readUserData(rs));
        }
        return users;
      }
    }
    catch (SQLException e) {
      throw new DataAccessException("Game data could not be retrieved: " + e.getMessage());
    }
  }


  /**
   * Deletes a user data entry at the given username key from the user table.
   *
   * @param key username of user data to be deleted
   * @throws DataAccessException  if SQL error thrown during deletion
   */
  @Override
  public void delete(String key) throws DataAccessException {
    try (var preparedStatement = connection.prepareStatement("DELETE FROM " + USER_TABLE + " WHERE username=?")) {
      preparedStatement.setString(1, key);
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
   * Truncates the user data table.
   *
   * @throws DataAccessException  if SQL error thrown during truncation
   */
  @Override
  public void clear() throws DataAccessException {
    try {
      executeUpdate("TRUNCATE TABLE " + USER_TABLE);
    }
    catch (SQLException e) {
      throw new DataAccessException(DataAccessException.ErrorMessages.BAD_REQUEST + ": " + e.getMessage());
    }
  }


  /**
   * Checks a password attempt against the password associated with the desired username.
   *
   * @param username        username of existing user
   * @param passwordAttempt attempted password for the existing user
   * @return                if the passwords match
   */
  @Override
  public boolean attemptPassword(String username, String passwordAttempt) {
    try {
      return encoder.matches(passwordAttempt, get(username).password());
    }
    catch (DataAccessException e) {
      throw new IllegalArgumentException("Bad username passed to attemptPassword");
    }
  }


  /**
   * Convert a result set containing user data into a UserData object.
   *
   * @param rs  result set containing user data
   * @return    UserData objected extracted from 'rs'
   * @throws SQLException if SQL thrown during conversion
   */
  private UserData readUserData(ResultSet rs) throws SQLException {
    try {
      String username = rs.getString(1);
      String password = rs.getString(2);
      String email = rs.getString(3);

      return new UserData(username, password, email);
    }
    catch (SQLException e) {
      throw new IllegalArgumentException("Bad result set passed to readUserData: " + e.getMessage());
    }
  }


  /**
   * Hash a given password with bcrypt.
   *
   * @param password  password to be hashed
   * @return          hashed password
   */
  private String hashPassword(String password) {
    return encoder.encode(password);
  }
}
