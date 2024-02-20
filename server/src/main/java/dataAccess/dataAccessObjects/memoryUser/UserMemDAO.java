package dataAccess.dataAccessObjects.memoryUser;

import dataAccess.DataAccessException;
import dataAccess.dataAccessObjects.UserDAO;
import model.UserData;

import java.util.Collections;
import java.util.Map;

public final class UserMemDAO implements UserDAO {

  private Map<String, UserData> userDataCollection = Collections.emptyMap();

  @Override
  public void clearData() {
    userDataCollection.clear();
  }

  @Override
  public void createUser(UserData userData) throws DataAccessException {
    UserData replacedUser = userDataCollection.put(userData.username(), userData);
    if (replacedUser == null) {
      throw new DataAccessException("User '" + userData.username() + "' already exists as a user");
    }
  }

  @Override
  public UserData getUser(String username) throws DataAccessException {
    UserData user = userDataCollection.get(username);
    if (user != null) {
      return user;
    }
    else {
      throw new DataAccessException("User '" + username + "' was not found");
    }
  }
}
