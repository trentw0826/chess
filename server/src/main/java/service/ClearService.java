package service;

import service.request.ClearRequest;
import service.response.ClearResponse;

/**
 * Clear service handles clear requests from the clear handler
 */
public class ClearService extends Service {

  /**
   * Default constructor
   */
  public ClearService() {
    super();
  }


  /**
   * Clears all the databases
   *
   * @param clearRequest  TODO this request isn't really necessary.
   * @return              clear response TODO this also isn't really necessary
   */
  public ClearResponse clear(ClearRequest clearRequest) {
    memoryUserDAO.clear();
    memoryAuthDAO.clear();
    memoryGameDAO.clear();

    return new ClearResponse();
  }
}
