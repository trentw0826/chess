package dataAccess;

import model.AuthData;

public interface AuthDAO extends DataAccessObject {

  /**
   * Create and insert new AuthData object into database.
   *
   * @param authData  the auth data to be inserted
   */
  void createAuth(AuthData authData);


  /**
   * Retrieve an AuthData object from the database based on given auth token.
   *
   * @return  the retrieved AuthData object
   * @throws DataAccessException  if the authToken is not associated with any AuthData object in the database
   */
  AuthData getAuth(String authToken) throws DataAccessException;


  /**
   * Delete an AuthData object from the database.
   *
   * @param authToken the associated auth token
   * @throws DataAccessException  if the authToken is not associated with any AuthData object in the database
   */
  void deleteAuth(String authToken) throws DataAccessException;
}
