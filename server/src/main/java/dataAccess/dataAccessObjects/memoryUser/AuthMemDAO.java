package dataAccess.dataAccessObjects.memoryUser;

import dataAccess.DataAccessException;
import dataAccess.dataAccessObjects.AuthDAO;
import model.AuthData;

import java.util.Collections;
import java.util.Map;

public final class AuthMemDAO implements AuthDAO {

  private Map<String, AuthData> authDataCollection = Collections.emptyMap();

  @Override
  public void clearData() {
    authDataCollection.clear();
  }

  @Override
  public void createAuth(AuthData authData) throws DataAccessException {
    AuthData replacedAuth = authDataCollection.put(authData.username(), authData);
    if (replacedAuth == null) {
      throw new DataAccessException("AuthToken '" + authData.username() + "' already exists");
    }
  }

  @Override
  public AuthData getAuth(String authToken) throws DataAccessException {
    AuthData user = authDataCollection.get(authToken);
    if (user != null) {
      return user;
    }
    else {
      throw new DataAccessException("AuthToken '" + authToken + "' was not found");
    }
  }

  @Override
  public void deleteAuth(String authToken) throws DataAccessException {

  }
}
