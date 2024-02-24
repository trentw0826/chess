package service;

import model.UserData;
import org.junit.jupiter.api.*;

import java.util.UUID;


class AuthServiceTest {

  private boolean isValidUUID(String uuidString) {
    try {
      UUID.fromString(uuidString);
    } catch (IllegalArgumentException e) {
      return false;
    }
    return true;
  }


  UserData testUser;
  String genericAuthToken;
  RegisterServiceResponse expectedRegisterResponse;
  RegisterServiceResponse actualRegisterResponse;
  UserService testUserService;


  @BeforeEach
  void setUp() {
    genericAuthToken = UUID.randomUUID().toString();
    testUserService = new UserService();

    testUser = null;
    RegisterServiceResponse expectedRegisterResponse = null;
    RegisterServiceResponse actualRegisterResponse = null;
  }


  @Test
  void successfulRegistration() {
    testUser = new UserData("someUsername", "somePassword", "someEmail");

    expectedRegisterResponse = new RegisterServiceResponse("someUsername", genericAuthToken);
    actualRegisterResponse = testUserService.register(testUser);

    Assertions.assertEquals(expectedRegisterResponse.getUsername(), actualRegisterResponse.getUsername());
    Assertions.assertTrue(isValidUUID(actualRegisterResponse.getAuthToken()));
  }


  @Test
  void alreadyTakenRegistration() {
    testUser = new UserData("someUsername", "somePassword", "someEmail");
    UserData existingUser = new UserData("someUsername", "somePassword", "someEmail");

    testUserService.register(existingUser);
    actualRegisterResponse = testUserService.register(testUser);

    expectedRegisterResponse = new RegisterServiceResponse( "Error: already taken");
    Assertions.assertEquals(expectedRegisterResponse, actualRegisterResponse);
  }
}