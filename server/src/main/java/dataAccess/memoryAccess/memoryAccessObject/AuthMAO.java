package dataAccess.memoryAccess.memoryAccessObject;

import dataAccess.memoryAccess.MemoryAccessObject;
import model.AuthData;
import model.AuthToken;

/**
 * Defines a data access object that accesses and modifies locally stored auth data
 */
public class AuthMAO extends MemoryAccessObject<AuthToken, AuthData> {

  /**
   * Returns the key of an AuthData object.
   *
   * @param data  auth data model
   * @return      key of 'data' (auth token)
   */
  @Override
  public AuthToken generateKey(AuthData data) {
    return data.authToken();
  }
}
