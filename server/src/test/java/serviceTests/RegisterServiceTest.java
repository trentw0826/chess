package serviceTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.RegisterService;
import service.ServiceConstants;
import service.request.RegisterRequest;
import service.response.RegisterResponse;

import static org.junit.jupiter.api.Assertions.*;
import static serviceTests.ServiceTestHelper.*;

class RegisterServiceTest {

  RegisterService testRegisterService;
  RegisterRequest testRegisterRequest;
  RegisterResponse expectedResponse;
  RegisterResponse actualResponse;
  
  @BeforeEach
  void setUp() {
    testRegisterService = new RegisterService();
    testRegisterService.clear();
    testRegisterRequest = null;
    expectedResponse = null;
    actualResponse = null;
  }

  @Test
  void normalRegisterTest() {
    testRegisterRequest = new RegisterRequest(USERNAME1, PASSWORD1, EMAIL1);
    expectedResponse = new RegisterResponse(USERNAME1, FILLERTEXT);

    actualResponse = testRegisterService.processHandlerRequest(testRegisterRequest);

    Assertions.assertEquals(expectedResponse.getUsername(), actualResponse.getUsername());
    Assertions.assertTrue(ServiceTestHelper.validAuthToken(actualResponse.getAuthToken()));
  }


  @Test
  void alreadyExistsRegisterTest() {
    testRegisterRequest = new RegisterRequest(USERNAME1, PASSWORD1, EMAIL1);
    expectedResponse = new RegisterResponse(ServiceConstants.ERROR_MESSAGES.ALREADY_TAKEN.message());

    testRegisterService.processHandlerRequest(testRegisterRequest);
    actualResponse = testRegisterService.processHandlerRequest(testRegisterRequest);

    Assertions.assertEquals(expectedResponse.getMessage(), actualResponse.getMessage());
    Assertions.assertFalse(ServiceTestHelper.validAuthToken(actualResponse.getAuthToken()));
  }
}