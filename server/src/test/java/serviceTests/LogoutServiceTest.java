package serviceTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.LogoutService;
import service.RegisterService;
import service.ServiceConstants;
import service.request.LogoutRequest;
import service.request.RegisterRequest;
import service.response.LogoutResponse;

import static serviceTests.ServiceTestHelper.*;

class LogoutServiceTest {

  LogoutService testLogoutService;
  LogoutRequest testLogoutRequest;
  LogoutResponse expectedResponse;
  LogoutResponse actualResponse;

  @BeforeEach
  void setUp() {
    testLogoutService = new LogoutService();
    testLogoutService.clear();
    testLogoutRequest = null;
    expectedResponse = null;
    actualResponse = null;
  }

  @Test
  void normalLogout() {
    RegisterRequest testRegisterRequest = new RegisterRequest(USERNAME1, PASSWORD1, EMAIL1);
    String authToken = new RegisterService().processHandlerRequest(testRegisterRequest).getAuthToken();
    
    testLogoutRequest = new LogoutRequest(authToken);
    expectedResponse = new LogoutResponse();

    actualResponse = testLogoutService.processHandlerRequest(testLogoutRequest);

    Assertions.assertEquals(expectedResponse.getMessage(), actualResponse.getMessage());
  }


  @Test
  void invalidLogout() {
    testLogoutRequest = new LogoutRequest(FILLERTEXT);
    expectedResponse = new LogoutResponse(ServiceConstants.ErrorMessages.UNAUTHORIZED.message());

    actualResponse = testLogoutService.processHandlerRequest(testLogoutRequest);

    Assertions.assertEquals(expectedResponse.getMessage(), actualResponse.getMessage());
  }
}