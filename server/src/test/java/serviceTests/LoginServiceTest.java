package serviceTests;

import dataAccess.DataAccessException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.LoginService;
import service.RegisterService;
import request.httpRequests.LoginRequest;
import request.httpRequests.RegisterRequest;
import response.LoginResponse;

import static serviceTests.ServiceTestHelper.*;

class LoginServiceTest {

  LoginService testLoginService;
  LoginRequest testLoginRequest;
  LoginResponse expectedResponse;
  LoginResponse actualResponse;

  @BeforeEach
  void setUp() {
    testLoginService = new LoginService();
    testLoginService.clear();
    testLoginRequest = null;
    expectedResponse = null;
    actualResponse = null;
  }


  @Test
  void normalLoginTest() {
    RegisterService testRegisterService = new RegisterService();
    testRegisterService.processHandlerRequest(new RegisterRequest(USERNAME1, PASSWORD1, EMAIL1));

    testLoginRequest = new LoginRequest(USERNAME1, PASSWORD1);
    expectedResponse = new LoginResponse(USERNAME1, FILLERTEXT);

    actualResponse = testLoginService.processHandlerRequest(testLoginRequest);

    Assertions.assertEquals(expectedResponse.getUsername(), actualResponse.getUsername());
    Assertions.assertTrue(ServiceTestHelper.validAuthToken(actualResponse.getAuthToken()));
  }


  @Test
  void nonExistingLoginTest() {
    testLoginRequest = new LoginRequest(USERNAME1, PASSWORD1);
    expectedResponse = new LoginResponse(DataAccessException.ErrorMessages.UNAUTHORIZED.message());

    actualResponse = testLoginService.processHandlerRequest(testLoginRequest);

    Assertions.assertEquals(expectedResponse.getMessage(), actualResponse.getMessage());
    Assertions.assertFalse(ServiceTestHelper.validAuthToken(actualResponse.getAuthToken()));
  }
}