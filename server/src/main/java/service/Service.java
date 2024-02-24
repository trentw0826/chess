package service;

import dataAccess.memoryAccess.memoryAccessObject.AuthMAO;
import dataAccess.memoryAccess.memoryAccessObject.GameMAO;
import dataAccess.memoryAccess.memoryAccessObject.UserMAO;
import service.response.ServiceResponse;

public abstract class Service {
//  /* singleton instance */
//  private static Service instance;

  /* local databases */
  protected static final UserMAO memoryUserDAO = new UserMAO();
  protected static final AuthMAO memoryAuthDAO = new AuthMAO();
  protected static final GameMAO memoryGameDAO = new GameMAO();

  abstract ServiceResponse clear();


//  /**
//   * @return singleton instance of Service class
//   */
//  public static Service getInstance() {
//    if (instance == null) {
//      instance = new Service();
//    }
//    return instance;
//  }


//  /**
//   * Clears all internal databases.
//   *
//   * @return  successful ClearResponse object
//   */
//  public ClearServiceResponse clearAllLocalDatabases() {
//    memoryUserDAO.clear();
//    memoryAuthDAO.clear();
//    memoryGameDAO.clear();
//
//    // TODO is ClearResponse object even necessary?
//    return new ClearServiceResponse();
//  }

}
