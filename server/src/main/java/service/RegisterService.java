package service;

import dataAccess.DataAccessException;
import model.AuthData;
import model.AuthToken;
import model.UserData;
import service.request.RegisterRequest;
import service.response.RegisterResponse;


public class RegisterService extends Service {

  /**
   * Default constructor
   */
  public RegisterService() {
    super();
  }

  /**
   * Takes a register request, converts it into a UserData object,
   * and attempts to register that user data in the local user memory database.
   * Returns a RegisterResponse object (successful if properly added, unsuccessful if exception
   * thrown during adding)
   *
   * @param registerRequest register request
   * @return                register response
   */
  public RegisterResponse register(RegisterRequest registerRequest) {
    RegisterResponse registerResponse;

    UserData newUsersData = new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email());

    try {
      memoryUserDAO.create(newUsersData);  // Block will break if user couldn't be registered
      AuthToken authToken = new AuthToken();
      memoryAuthDAO.create(new AuthData(authToken, registerRequest.username()));
      registerResponse = new RegisterResponse(newUsersData.username(), authToken); // Successful register
    }
    catch (DataAccessException e) {
      registerResponse = new RegisterResponse(e.getMessage()); // Unsuccessful register
    }

    return registerResponse;
  }
}
