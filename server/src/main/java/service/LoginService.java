package service;

import dataAccess.DataAccessException;
import model.AuthData;
import model.AuthToken;
import model.UserData;
import service.request.LoginRequest;
import service.response.LoginResponse;

/**
 * Login service handles login requests from the login handler
 */
public class LoginService extends Service {

  /**
   * Default constructor
   */
  public LoginService() {
    super();
  }


  /**
   * Takes a login request, converts it into a AuthData object,
   * and checks if the username exists in the database (if not, bad request)
   * If so, checks if the given password matches that of the associated username (if not, unauthorized)
   *
   * @param loginRequest login request
   * @return                login response
   */
  public LoginResponse login(LoginRequest loginRequest) {
    LoginResponse loginResponse;

    try {
      UserData retrievedUserData = memoryUserDAO.get(loginRequest.username()); // Block will break here if username doesn't exist
      if (!memoryUserDAO.attemptPassword(loginRequest.username(), loginRequest.password())) {
        throw new DataAccessException("Error: unauthorized");
      }

      // Valid new auth data for an existing user logging in
      AuthData newAuthData = new AuthData(new AuthToken(), retrievedUserData.username());
      memoryAuthDAO.create(newAuthData);
      loginResponse = new LoginResponse(newAuthData.username(), newAuthData.authToken());
    }
    catch (DataAccessException e) {
      loginResponse = new LoginResponse(e.getMessage());
    }

    return loginResponse;
  }
}
