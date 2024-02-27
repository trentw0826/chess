package service;

import dataAccess.DataAccessException;
import model.AuthData;
import model.UserData;
import service.request.LoginRequest;
import service.response.LoginResponse;


public class LoginService extends Service <LoginRequest, LoginResponse> {

  /**
   * Handles the login service for a given login request. If the login request
   * contains an existing username with its matching password, returns a login response
   * with newly generated auth data. Otherwise, returns a negative login response.
   *
   * @param loginRequest  login request
   * @return              login response
   */
  @Override
  public LoginResponse processHandlerRequest(LoginRequest loginRequest) {
    try {
      UserData retrievedUserData = USER_DAO.get(loginRequest.username());  // Try to get user data
      checkPassword(retrievedUserData, loginRequest.password()); // Try to validate password

      AuthData newAuthData = generateAuthData(retrievedUserData);
      return new LoginResponse(newAuthData.username(), newAuthData.authToken());
    }
    catch (DataAccessException e) {
      return new LoginResponse(ServiceConstants.ErrorMessages.UNAUTHORIZED.message());
    }
  }


  private void checkPassword(UserData userData, String password) throws DataAccessException {
    if (!USER_DAO.attemptPassword(userData.username(), password)) {
      throw new DataAccessException(ServiceConstants.ErrorMessages.UNAUTHORIZED);
    }
  }


  private AuthData generateAuthData(UserData userData) throws DataAccessException {
    String authToken = generateNewAuthToken();
    AUTH_DAO.create(new AuthData(authToken, userData.username()));
    return new AuthData(authToken, userData.username());
  }
}
