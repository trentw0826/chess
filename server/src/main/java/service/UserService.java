package service;

import dataAccess.DataAccessException;

import model.AuthData;
import model.UserData;
import service.response.RegisterServiceResponse;

import java.util.UUID;

/**
 * Handles the user service functionality:
 * Register, //TODO note other functionalities of UserService class
 */
public class UserService extends Service {

  /**
   * Handles the registering of a new user.
   *
   * @param user  the user data to be registered
   * @return      the register response (success or failure)
   */
  public RegisterServiceResponse register(UserData user) {
    try {
      memoryUserDAO.create(user);
      String authToken = memoryAuthDAO.create(new AuthData(UUID.randomUUID().toString(), user.username()));

      return new RegisterServiceResponse(user.username(), authToken);
    }
    catch (DataAccessException e) {
      return new RegisterServiceResponse(false, e.toString());
    }
  }


}
