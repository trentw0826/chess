package serviceTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.LoginService;
import service.LogoutService;
import service.RegisterService;
import service.Service;
import service.request.ServiceRequest;
import service.response.ServiceResponse;

import static org.junit.jupiter.api.Assertions.*;

class ClearServiceTest {

  ServiceRequest testClearRequest;
  ServiceResponse actualResponse1;
  ServiceResponse actualResponse2;
  ServiceResponse actualResponse3;
  ServiceResponse expectedResponse1;
  ServiceResponse expectedResponse2;
  ServiceResponse expectedResponse3;
  Service service1 = new RegisterService();
  Service service2 = new LoginService();
  Service service3 = new LogoutService();


  @BeforeEach
  void setUp() {
    testClearRequest = null;
    actualResponse1 = null;
    actualResponse2 = null;
    actualResponse3 = null;
    expectedResponse1 = null;
    expectedResponse2 = null;
    expectedResponse3 = null;
    service1 = new RegisterService();
    service2 = new LoginService();
    service3 = new LogoutService();
  }

  @Test
  void clearTest() {
    expectedResponse1 = new ServiceResponse();
    expectedResponse2 = new ServiceResponse();
    expectedResponse3 = new ServiceResponse();

    actualResponse1 = service1.clear();
    actualResponse2 = service2.clear();
    actualResponse3 = service3.clear();

    assertEquals(expectedResponse1.getMessage(), actualResponse1.getMessage());
    assertEquals(expectedResponse2.getMessage(), actualResponse2.getMessage());
    assertEquals(expectedResponse3.getMessage(), actualResponse3.getMessage());
  }
}