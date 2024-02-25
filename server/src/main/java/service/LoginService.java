package service;

import dataAccess.DataAccessException;
import model.AuthData;
import model.AuthToken;
import model.UserData;
import service.request.LoginRequest;
import service.response.LoginResponse;


public class LoginService extends Service {

  /**
   * Default constructor
   */
  public LoginService() {
    super();
  }


  /**
   * Handles the login service for a given login request. If the login request
   * contains an existing username with its matching password, returns a login response
   * with newly generated auth data.
   *
   * @param loginRequest  login request
   * @return              login response
   */
  public LoginResponse login(LoginRequest loginRequest) {
    LoginResponse loginResponse;

    try {
      UserData retrievedUserData = memoryUserDAO.get(loginRequest.username());
      if (!memoryUserDAO.attemptPassword(retrievedUserData.username(), loginRequest.password())) {
        throw new DataAccessException("Error: unauthorized");
      }

      // Successful login
      AuthData newAuthData = new AuthData(new AuthToken(), retrievedUserData.username());
      memoryAuthDAO.create(newAuthData);

      loginResponse = new LoginResponse(newAuthData.username(), newAuthData.authToken());
    }
    catch (DataAccessException e) {
      // Unsuccessful login
      loginResponse = new LoginResponse(e.getMessage());
    }

    return loginResponse;
  }
}
