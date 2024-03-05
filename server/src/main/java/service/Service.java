package service;


import dataAccess.DataAccessException;
import dataAccess.memoryAccess.memoryAccessObject.*;
import service.request.ServiceRequest;
import service.response.ServiceResponse;

import java.util.UUID;

/**
 * Parent class for Service classes.
 */
public abstract class Service <U extends ServiceRequest, T extends ServiceResponse> {

  /* local databases */
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
  protected static String generateNewAuthToken() {
    return UUID.randomUUID().toString();
  }


  /**
   * Checks if an auth token currently exists.
   *
   * @param authToken auth token
   * @return          true if given auth token exists in Auth database
   */
  protected boolean invalidAuthToken(String authToken) {
    try {
      AUTH_DAO.get(authToken);
    }
    catch (DataAccessException e) {
      return true;
    }
    return false;
  }
}
