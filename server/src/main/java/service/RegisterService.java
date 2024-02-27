package service;

import dataAccess.DataAccessException;
import model.AuthData;
import model.UserData;
import service.request.RegisterRequest;
import service.response.RegisterResponse;


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
        throw new DataAccessException(ServiceConstants.ErrorMessages.BAD_REQUEST);
      }

      USER_DAO.create(newUsersData);  // Block will break if user couldn't be registered

      String newAuthToken = generateNewAuthToken();
      AUTH_DAO.create(new AuthData(newAuthToken, registerRequest.username()));

      registerResponse = new RegisterResponse(newUsersData.username(), newAuthToken); // Successful register
    }
    catch (DataAccessException e) {
      registerResponse = new RegisterResponse(e.getMessage()); // Unsuccessful register
    }

    return registerResponse;
  }
}
