package service;

import dataAccess.DataAccessException;
import dataAccess.memoryAccess.MemoryAuthDAO;
import dataAccess.memoryAccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;
import response.RegisterResponse;
import response.ServiceResponse;

import java.util.UUID;

//TODO Make all service classes singleton
public class UserService {
  private final MemoryUserDAO memoryUserDAO = new MemoryUserDAO();
  private final MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();

  public ServiceResponse register(UserData user) {
    try {
      memoryUserDAO.create(user);
      String authToken = memoryAuthDAO.create(new AuthData(UUID.randomUUID().toString(), user.username()));

      return new RegisterResponse(user.username(), authToken);
    }
    catch (DataAccessException e) {
      return new ServiceResponse(false, e.toString());
    }
  }

//  public AuthData login(UserData user) {}
//  public void logout(UserData user) {}
}
