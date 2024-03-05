package dataAccess.memoryAccess.memoryAccessObject;

import dataAccess.DataAccessException;
import dataAccess.memoryAccess.MemoryAccessObject;
import model.AuthData;

/**
 * Defines a data access object that accesses and modifies locally stored auth data.
 */
public class AuthMao extends MemoryAccessObject<String, AuthData> {

  /**
   * Gets a username based on a provided auth token.
   *
   * @param authToken auth token
   * @return username associated with 'authToken'
   * @throws DataAccessException if 'authToken' isn't an active auth token
   */
  public String getUsernameFromAuthToken(String authToken) throws DataAccessException {
    try {
      return get(authToken).username();
    } catch (DataAccessException e) {
      throw new DataAccessException(DataAccessException.ErrorMessages.UNAUTHORIZED);
    }
  }
}
