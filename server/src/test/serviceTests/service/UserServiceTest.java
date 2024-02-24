package service;

import model.UserData;
import org.junit.jupiter.api.*;
import response.RegisterResponse;

import java.util.UUID;


class UserServiceTest {

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
  RegisterResponse expectedRegisterResponse;
  RegisterResponse actualRegisterResponse;
  UserService testUserService;


  @BeforeEach
  void setUp() {
    genericAuthToken = UUID.randomUUID().toString();
    testUserService = new UserService();

    testUser = null;
    RegisterResponse expectedRegisterResponse = null;
    RegisterResponse actualRegisterResponse = null;
  }


  @Test
  void successfulRegistration() {
    testUser = new UserData("someUsername", "somePassword", "someEmail");

    expectedRegisterResponse = new RegisterResponse("someUsername", genericAuthToken);
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

    expectedRegisterResponse = new RegisterResponse(false, "Error: already taken");
    Assertions.assertEquals(expectedRegisterResponse, actualRegisterResponse);
  }
}