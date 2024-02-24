package service;

import dataAccess.DataAccessException;

import model.AuthData;
import model.UserData;
import service.response.ServiceResponse;
import service.response.userServiceResponse.RegisterServiceResponse;

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
      return new RegisterServiceResponse(ServiceResponse.ERROR_MESSAGE.ERROR_BAD_REQUEST); //TODO this call is wrong
    }
  }

  @Override
  public ServiceResponse clear() {
    memoryUserDAO.clear();
    return new ServiceResponse();
  }
}
