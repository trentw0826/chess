package dataAccess;

import model.UserData;

public interface UserDao extends DataAccessObject<String, UserData> {
  /**
   * Returns if a password attempt is valid for the given username.
   *
   * @param username        username to attempt password for
   * @param passwordAttempt password attempt
   * @return                if the attempted password matches the associated one
   */
  boolean attemptPassword(final String username, final String passwordAttempt);
}
