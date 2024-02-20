package dataAccess.memoryAccess;

import model.AuthData;

/**
 * Defines a data access object that accesses and modifies locally stored auth data
 */
public class MemoryAuthDAO extends MemoryDAO<AuthData, String> {

  /**
   * Returns the key of an AuthData object.
   *
   * @param data  auth data model
   * @return      key of 'data' (auth token)
   */
  @Override
  String generateKey(AuthData data) {
    return data.authToken();
  }
}
