package service;


import dataAccess.*;
import dataAccess.databaseAccess.sqlAccessObject.AuthSqlDao;
import dataAccess.databaseAccess.sqlAccessObject.GameSqlDao;
import dataAccess.databaseAccess.sqlAccessObject.UserSqlDao;
import dataAccess.memoryAccess.memoryAccessObject.AuthMao;
import dataAccess.memoryAccess.memoryAccessObject.GameMao;
import dataAccess.memoryAccess.memoryAccessObject.UserMao;
import service.request.ServiceRequest;
import service.response.ServiceResponse;

import java.util.UUID;

/**
 * Parent class for Service classes.
 */
public abstract class Service <U extends ServiceRequest, T extends ServiceResponse> {

  /* Database Access Objects */
  protected final UserDao USER_DAO;
  protected final GameDao GAME_DAO;
  protected final AuthDao AUTH_DAO;


  protected Service() {
    /* Remote SQL data storage */
    USER_DAO = new UserSqlDao();
    AUTH_DAO = new AuthSqlDao();
    GAME_DAO = new GameSqlDao();

    /* Local data storage */
//    USER_DAO = new UserMao();
//    AUTH_DAO = new AuthMao();
//    GAME_DAO = new GameMao();
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
      USER_DAO.clear();
      AUTH_DAO.clear();
      GAME_DAO.clear();
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
      AUTH_DAO.get(authToken);
    }
    catch (DataAccessException e) {
      return true;
    }
    return false;
  }
}
