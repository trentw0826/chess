package dataAccess.memoryAccess.memoryAccessObject;

import dataAccess.memoryAccess.MemoryAccessObject;
import model.AuthData;

/**
 * Defines a data access object that accesses and modifies locally stored auth data
 */
public class AuthMAO extends MemoryAccessObject<String, AuthData> {

  /**
   * Returns the key of an AuthData object.
   *
   * @param data  auth data model
   * @return      key of 'data' (auth token)
   */
  @Override
  public String generateKey(AuthData data) {
    return data.authToken();
  }
}
