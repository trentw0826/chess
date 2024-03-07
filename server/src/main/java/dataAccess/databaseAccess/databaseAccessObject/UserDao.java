package dataAccess.databaseAccess.databaseAccessObject;

import dataAccess.DataAccessException;
import dataAccess.databaseAccess.DatabaseAccessObject;
import model.UserData;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;


public class UserDao extends DatabaseAccessObject<String, UserData> {

  private static final String USER_DATABASE_NAME = "userdata";

  public UserDao() throws DataAccessException {}


  @Override
  public String create(UserData data) throws DataAccessException {
    try (var preparedStatement = connection.prepareStatement("INSERT INTO " + USER_DATABASE_NAME +
            " (username, password, email) VALUES(?, ?, ?)")) {

      preparedStatement.setString(1, data.username());
      preparedStatement.setString(2, data.password());
      preparedStatement.setString(3, data.email());

      preparedStatement.executeUpdate();

      return data.username();
    }
    catch (SQLException ex) {
      throw new DataAccessException(DataAccessException.ErrorMessages.BAD_REQUEST + ": " + ex.getMessage());
    }
  }

  @Override
  public UserData get(String key) throws DataAccessException {
    return null;
  }

  @Override
  public Collection<UserData> listData() {
    return Collections.emptyList();
  }

  @Override
  public void delete(String key) throws DataAccessException {

  }

  @Override
  public void clear() throws DataAccessException {
    try (var preparedStatement = connection.prepareStatement("TRUNCATE TABLE " + USER_DATABASE_NAME)) {
      preparedStatement.executeUpdate();
    }
    catch (SQLException ex) {
      throw new DataAccessException(DataAccessException.ErrorMessages.BAD_REQUEST + ": " + ex.getMessage());
    }
  }

  public boolean attemptPassword(String username, String password) {
    return false;
  }
}
