package dataAccess.memoryAccess;

import model.UserData;

/**
 * Defines a data access object that accesses and modifies locally stored user data
 */
public class MemoryUserDAO extends MemoryDAO<String, UserData> {

  /**
   * Returns the username of a user data model as its key
   *
   * @param data  user data model
   * @return      key of 'data' (username)
   */
  @Override
  String generateKey(UserData data) {
    return data.username();
  }
}
