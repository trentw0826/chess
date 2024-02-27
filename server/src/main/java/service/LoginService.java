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
   * with newly generated auth data.
   *
   * @param loginRequest  login request
   * @return              login response
   */
  @Override
  public LoginResponse processHandlerRequest(LoginRequest loginRequest) {
    try {
      UserData retrievedUserData = getUserData(loginRequest.username());  // Get user data
      validateUserAndPassword(retrievedUserData, loginRequest.password()); // Validate user data

      AuthData newAuthData = generateAuthData(retrievedUserData);
      return new LoginResponse(newAuthData.username(), newAuthData.authToken());
    }
    catch (DataAccessException e) {
      return new LoginResponse(e.getMessage());
    }
  }


  /**
   * Helper method that retrieves a user's data.
   *
   * @param username  username at which to retrieve data
   * @return          retrieved user data
   * @throws DataAccessException  if userdata under 'username' doesn't exist
   */
  private UserData getUserData(String username) throws DataAccessException {
    UserData userData = USER_DAO.get(username);
    if (userData == null || userData.hasNullFields()) {
      throw new DataAccessException(ServiceConstants.ERROR_MESSAGES.BAD_REQUEST);
    }
    return userData;
  }


  private void validateUserAndPassword(UserData userData, String password) throws DataAccessException {
    if (!USER_DAO.attemptPassword(userData.username(), password)) {
      throw new DataAccessException(ServiceConstants.ERROR_MESSAGES.UNAUTHORIZED);
    }
  }


  private AuthData generateAuthData(UserData userData) throws DataAccessException {
    String authToken = generateNewAuthToken();
    AUTH_DAO.create(new AuthData(authToken, userData.username()));
    return new AuthData(authToken, userData.username());
  }
}
