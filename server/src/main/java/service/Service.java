package service;


import dataAccess.*;
import dataAccess.dataAccessObject.AuthDao;
import dataAccess.dataAccessObject.GameDao;
import dataAccess.dataAccessObject.UserDao;
import dataAccess.sqlAccess.sqlAccessObjects.AuthSqlDao;
import dataAccess.sqlAccess.sqlAccessObjects.GameSqlDao;
import dataAccess.sqlAccess.sqlAccessObjects.UserSqlDao;
import request.httpRequests.ServiceRequest;
import response.ServiceResponse;

import java.util.UUID;

/**
 * Parent class for Service classes.
 */
public abstract class Service <U extends ServiceRequest, T extends ServiceResponse> {

  /* Database Access Objects */
  protected final UserDao userDao;
  protected final GameDao gameDao;
  protected final AuthDao authDao;


  protected Service() {
    // Data storage
    userDao = new UserSqlDao();
    authDao = new AuthSqlDao();
    gameDao = new GameSqlDao();
  }


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
      userDao.clear();
      authDao.clear();
      gameDao.clear();
    }
    catch (DataAccessException e) {
      return new ServiceResponse(e.getMessage());
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
  protected boolean invalidAuthToken(final String authToken) {
    try {
      authDao.get(authToken);
    }
    catch (DataAccessException e) {
      return true;
    }
    return false;
  }
}
