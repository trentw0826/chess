package dataAccess.memoryAccess.memoryAccessObject;

import dataAccess.DataAccessException;
import dataAccess.memoryAccess.MemoryAccessObject;
import model.UserData;

import java.util.Objects;

/**
 * Defines a data access object that accesses and modifies locally stored user data
 */
public class UserMAO extends MemoryAccessObject<String, UserData> {

  /**
   * Returns the username of a user data model as its key
   *
   * @param data  user data model
   * @return      key of 'data' (username)
   */
  @Override
  public String generateKey(UserData data) {
    return data.username();
  }


  /**
   * Returns if a password attempt is valid for the given username.
   *
   * @param username        username to attempt password for
   * @param passwordAttempt password attempt
   * @return                if the attempted password matches the associated one
   */
  public boolean attemptPassword(String username, String passwordAttempt) {
    try {
      return Objects.equals(get(username).password(), passwordAttempt);
    }
    catch (DataAccessException e) {
      throw new IllegalArgumentException("Nonexistent username passed");
    }
  }
}
