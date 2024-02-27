package service;


import dataAccess.DataAccessException;
import dataAccess.memoryAccess.memoryAccessObject.*;
import service.request.ServiceRequest;
import service.response.ServiceResponse;

import java.util.UUID;

/**
 * Parent class for Service classes.
 */
//TODO Using generics, extract common functionality
//TODO migrate all request and response objects to their respective classes?
//TODO Define some list of common error messages
public abstract class Service <REQUEST_TYPE extends ServiceRequest, RESPONSE_TYPE extends ServiceResponse> {

  /* local databases */
  // Update from new MemoryAccessObject to new DatabaseAccessObject next phase
  protected static final UserMAO USER_DAO = new UserMAO();
  protected static final AuthMAO AUTH_DAO = new AuthMAO();
  protected static final GameMAO GAME_DAO = new GameMAO();


  protected Service() {}

  protected abstract RESPONSE_TYPE processHandlerRequest(REQUEST_TYPE serviceRequest);

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
  // TODO Find where else this logic should be replaced
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
