package dataAccess.memoryAccess.memoryAccessObject;

import dataAccess.DataAccessException;
import dataAccess.memoryAccess.MemoryAccessObject;
import model.AuthData;
import service.ServiceConstants;

/**
 * Defines a data access object that accesses and modifies locally stored auth data.
 */
public class AuthMao extends MemoryAccessObject<String, AuthData> {

  /**
   * Returns the key of an AuthData object.
   *
   * @param data auth data model
   * @return key of 'data' (auth token)
   */
  @Override
  public String generateKey(AuthData data) {
    return data.authToken();
  }


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
      throw new DataAccessException(ServiceConstants.ERROR_MESSAGES.UNAUTHORIZED);
    }
  }
}
