package service;

import dataAccess.DataAccessException;
import service.request.LogoutRequest;
import service.response.LogoutResponse;


public class LogoutService extends Service {

  /**
   * Default constructor
   */
  public LogoutService() {
    super();
  }


  /**
   * Handles the logout service for a given logout request.
   *
   * @param logoutRequest logout request
   * @return              logout response
   */
  public LogoutResponse logout(LogoutRequest logoutRequest) {
    LogoutResponse logoutResponse;

    try {
      memoryAuthDAO.delete(logoutRequest.authToken());
      logoutResponse = new LogoutResponse();
    }
    catch (DataAccessException e) {
      logoutResponse = new LogoutResponse(e.getMessage());
    }

    return logoutResponse;
  }
}
