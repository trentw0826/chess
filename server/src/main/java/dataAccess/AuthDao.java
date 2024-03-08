package dataAccess;

import model.AuthData;


/**
 * Defines the functionality of a DAO that interacts with auth data.
 */
public interface AuthDao extends DataAccessObject<String, AuthData> {
  /**
   * Gets a username based on a provided auth token.
   *
   * @param authToken auth token
   * @return username associated with 'authToken'
   * @throws DataAccessException if 'authToken' isn't an active auth token
   */
  String getUsernameFromAuthToken(final String authToken) throws DataAccessException;
}
