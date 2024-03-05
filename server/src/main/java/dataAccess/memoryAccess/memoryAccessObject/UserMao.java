package dataAccess.memoryAccess.memoryAccessObject;

import dataAccess.DataAccessException;
import dataAccess.memoryAccess.MemoryAccessObject;
import model.UserData;

import java.util.Objects;

/**
 * Defines a data access object that accesses and modifies locally stored user data
 */
public class UserMao extends MemoryAccessObject<String, UserData> {

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
