package service;

import dataAccess.DataAccessException;
import service.request.LogoutRequest;
import service.response.LogoutResponse;


public class LogoutService extends Service <LogoutRequest, LogoutResponse> {

  /**
   * Handles the logout service for a given logout request.
   *
   * @param logoutRequest logout request
   * @return              logout response
   */
  @Override
  public LogoutResponse processHandlerRequest(LogoutRequest logoutRequest) {
    LogoutResponse logoutResponse;

    try {
      AUTH_DAO.delete(logoutRequest.authToken());
      logoutResponse = new LogoutResponse();
    }
    catch (DataAccessException e) {
      logoutResponse = new LogoutResponse(e.getMessage());
    }

    return logoutResponse;
  }
}
