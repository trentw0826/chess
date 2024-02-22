package service;

import dataAccess.DataAccessException;

import model.AuthData;
import model.UserData;
import response.RegisterResponse;

import java.util.UUID;

public class UserService extends Service {

  public RegisterResponse register(UserData user) {
    try {
      memoryUserDAO.create(user);
      String authToken = memoryAuthDAO.create(new AuthData(UUID.randomUUID().toString(), user.username()));
      return new RegisterResponse(user.username(), authToken);
    }
    catch (DataAccessException e) {
      return new RegisterResponse(false, e.toString());
    }
  }
}
