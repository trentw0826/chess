package serviceTests;

import org.eclipse.jetty.util.log.Log;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.LoginService;
import service.RegisterService;
import service.request.LoginRequest;
import service.request.RegisterRequest;
import service.response.LoginResponse;

import static org.junit.jupiter.api.Assertions.*;
import static serviceTests.ServiceTestHelper.*;

class LoginServiceTest {

  LoginService testLoginService;
  LoginRequest testLoginRequest;
  LoginResponse expectedResponse;
  LoginResponse actualResponse;

  @BeforeEach
  void setUp() {
    testLoginService = new LoginService();
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
    expectedResponse = new LoginResponse("Error: unauthorized");

    actualResponse = testLoginService.processHandlerRequest(testLoginRequest);

    Assertions.assertEquals(expectedResponse.getMessage(), actualResponse.getMessage());
    Assertions.assertFalse(ServiceTestHelper.validAuthToken(actualResponse.getAuthToken()));
  }
}