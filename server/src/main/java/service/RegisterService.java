package service;

import dataAccess.DataAccessException;
import model.AuthData;
import model.UserData;
import request.RegisterRequest;
import response.RegisterResponse;


public class RegisterService extends Service <RegisterRequest, RegisterResponse> {

  /**
   * Handles a register request.
   * Attempts to register the provided user in the local user memory database.
   * Returns a RegisterResponse object (successful if properly added, unsuccessful if exception
   * thrown during adding)
   *
   * @param registerRequest register request
   * @return                register response
   */
  @Override
  public RegisterResponse processHandlerRequest(RegisterRequest registerRequest) {
    RegisterResponse registerResponse;

    UserData newUsersData = new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email());

    try {
      if (newUsersData.hasNullFields()) {
        throw new DataAccessException(DataAccessException.ErrorMessages.BAD_REQUEST);
      }

      userDao.create(newUsersData);  // Block will break if user couldn't be registered

      String newAuthToken = generateNewAuthToken();
      authDao.create(new AuthData(newAuthToken, registerRequest.username()));

      registerResponse = new RegisterResponse(newUsersData.username(), newAuthToken); // Successful register
    }
    catch (DataAccessException e) {
      registerResponse = new RegisterResponse(e.getMessage()); // Unsuccessful register
    }

    return registerResponse;
  }
}
