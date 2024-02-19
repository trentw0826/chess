package dataAccess;

import model.UserData;

public interface UserDao extends DataAccessObject{

  /**
   * Create a new user object in the database.
   *
   * @param userData  the user data to be inserted
   */
  void createUser(UserData userData);


  /**
   * Retrieves a user object from the database.
   *
   * @param username  the user's username
   * @return          the user object associated with the given username
   * @throws DataAccessException  if the username is not associated with any user object in the database
   */
  UserData getUser(String username) throws DataAccessException;

}
