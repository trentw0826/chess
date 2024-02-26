package service;

import service.request.ServiceRequest;
import service.response.ServiceResponse;

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
   * @param clearRequest  generic service request
   * @return              generic service response
   */
  //TODO Completely remove this request?
  public ServiceResponse clear(ServiceRequest clearRequest) {
    memoryUserDAO.clear();
    memoryAuthDAO.clear();
    memoryGameDAO.clear();

    return new ServiceResponse();
  }
}
