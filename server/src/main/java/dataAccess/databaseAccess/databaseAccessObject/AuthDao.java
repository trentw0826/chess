package dataAccess.databaseAccess.databaseAccessObject;

import dataAccess.DataAccessException;
import dataAccess.databaseAccess.DatabaseAccessObject;
import model.AuthData;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class AuthDao extends DatabaseAccessObject<String, AuthData> {

  private static final String AUTH_DATABASE_NAME = "authdata";

  public AuthDao() throws DataAccessException {}

  @Override
  public String create(AuthData data) throws DataAccessException {
    
    try (var preparedStatement = connection.prepareStatement("INSERT INTO " + AUTH_DATABASE_NAME +
            " (authToken, username) VALUES(?, ?)", RETURN_GENERATED_KEYS)) {

      preparedStatement.setString(1, data.authToken());
      preparedStatement.setString(2, data.username());

      preparedStatement.executeUpdate();

      return data.authToken();
    }
    catch (SQLException ex) {
      throw new DataAccessException(DataAccessException.ErrorMessages.BAD_REQUEST + ": " + ex.getMessage());
    }
  }

  @Override
  public AuthData get(String key) throws DataAccessException {
    return null;
  }

  @Override
  public Collection<AuthData> listData() {
    return Collections.emptyList();
  }

  @Override
  public void delete(String key) throws DataAccessException {

  }

  @Override
  public void clear() throws DataAccessException {
    try (var preparedStatement = connection.prepareStatement("TRUNCATE TABLE " + AUTH_DATABASE_NAME)) {
      preparedStatement.executeUpdate();
    }
    catch (SQLException ex) {
      throw new DataAccessException(DataAccessException.ErrorMessages.BAD_REQUEST + ": " + ex.getMessage());
    }
  }

  //TODO Extract common definition between authdao and authmao?
  public String getUsernameFromAuthToken(String authToken) {
    return "";
  }
}
