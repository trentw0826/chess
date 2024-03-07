package service;


import dataAccess.DataAccessException;
import dataAccess.databaseAccess.databaseAccessObject.AuthDao;
import dataAccess.databaseAccess.databaseAccessObject.GameDao;
import dataAccess.databaseAccess.databaseAccessObject.UserDao;
import dataAccess.memoryAccess.memoryAccessObject.*;
import service.request.ServiceRequest;
import service.response.ServiceResponse;

import java.util.UUID;

/**
 * Parent class for Service classes.
 */
public abstract class Service <U extends ServiceRequest, T extends ServiceResponse> {

  /*
   * Attempt to load local database access objects
   */
  protected static final UserDao USER_DAO;
  protected static final GameDao GAME_DAO;
  protected static final AuthDao AUTH_DAO;

  static {
    try {
      USER_DAO = new UserDao();
      AUTH_DAO = new AuthDao();
      GAME_DAO = new GameDao();
    }
    catch (DataAccessException ex) {
      throw new IllegalStateException(ex);
    }
  }


  protected Service() {}

  /**
   * Handle the given service request.
   *
   * @param serviceRequest  service request
   * @return                corresponding service response
   */
  protected abstract T processHandlerRequest(U serviceRequest);


  /**
   * Clear all databases.
   *
   * @return  successful service response
   */
  public ServiceResponse clear() {
    try {
      USER_DAO.clear();
      AUTH_DAO.clear();
      GAME_DAO.clear();
    }
    catch (DataAccessException ex) {
      return new ServiceResponse(ex.getMessage());
    }

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
