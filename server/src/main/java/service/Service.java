package service;


import dataAccess.DataAccessException;
import dataAccess.memoryAccess.memoryAccessObject.*;
import service.request.ServiceRequest;
import service.response.ServiceResponse;

import java.util.UUID;

/**
 * Parent class for Service classes.
 */
// migrate all request and response objects to their respective classes?
// Define some list of common error messages
public abstract class Service <U extends ServiceRequest, T extends ServiceResponse> {

  /* local databases */
  // Update from new MemoryAccessObject to new DatabaseAccessObject next phase
  protected static final UserMao USER_DAO = new UserMao();
  protected static final AuthMao AUTH_DAO = new AuthMao();
  protected static final GameMao GAME_DAO = new GameMao();


  protected Service() {}

  protected abstract T processHandlerRequest(U serviceRequest);

  /**
   * Clear all databases.
   *
   * @return  successful service response
   */
  public ServiceResponse clear() {
    USER_DAO.clear();
    AUTH_DAO.clear();
    GAME_DAO.clear();

    return new ServiceResponse();
  }

  /**
   * @return  a newly generated auth token from random UUID
   */
  public static String generateNewAuthToken() {
    return UUID.randomUUID().toString();
  }


  /**
   * Checks if an auth token currently exists.
   *
   * @param authToken auth token
   * @return          true if given auth token exists in Auth database
   */
  protected boolean authTokenExists(String authToken) {
    try {
      AUTH_DAO.get(authToken);
    }
    catch (DataAccessException e) {
      return false;
    }
    return true;
  }
}
