package serviceTests;

import dataAccess.DataAccessException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.CreateGameService;
import service.RegisterService;
import request.CreateGameRequest;
import request.RegisterRequest;
import response.CreateGameResponse;


import static serviceTests.ServiceTestHelper.*;

class CreateGameServiceTest {

  static CreateGameService testCreateGameService;
  CreateGameRequest testCreateGameRequest;
  CreateGameResponse expectedResponse;
  CreateGameResponse actualResponse;
  String existingAuthToken;

  @BeforeAll
  static void beforeAll() {
    testCreateGameService = new CreateGameService();
    testCreateGameService.clear();
  }

  @BeforeEach
  void setUp() {
    testCreateGameService = new CreateGameService();
    testCreateGameService.clear();
    testCreateGameRequest = null;
    expectedResponse = null;
    actualResponse = null;

    // register user and grab their auth token
    RegisterService testRegisterService = new RegisterService();
    existingAuthToken = testRegisterService.processHandlerRequest(new RegisterRequest(USERNAME1, PASSWORD1, EMAIL1)).getAuthToken();
  }


  @Test
  void normalCreateGameTest() {
    testCreateGameRequest = new CreateGameRequest(GAMENAME1, existingAuthToken);
    expectedResponse = new CreateGameResponse(1);

    actualResponse = testCreateGameService.processHandlerRequest(testCreateGameRequest);

    Assertions.assertEquals(expectedResponse.getGameID(), actualResponse.getGameID());
  }


  @Test
  void invalidAuthTokenCreateGameTest() {
    testCreateGameRequest = new CreateGameRequest(GAMENAME1, "someinvalidtoken123");
    expectedResponse = new CreateGameResponse("Error: unauthorized");

    actualResponse = testCreateGameService.processHandlerRequest(testCreateGameRequest);

    Assertions.assertEquals(expectedResponse.getMessage(), actualResponse.getMessage());
  }


  @Test
  void alreadyExistingNameCreateGameTest() {
    testCreateGameRequest = new CreateGameRequest(GAMENAME1, existingAuthToken);
    expectedResponse = new CreateGameResponse(DataAccessException.ErrorMessages.ALREADY_TAKEN.message());

    CreateGameResponse throwawayResponse = testCreateGameService.processHandlerRequest(testCreateGameRequest);
    actualResponse = testCreateGameService.processHandlerRequest(testCreateGameRequest);

    Assertions.assertEquals(expectedResponse.getMessage(), actualResponse.getMessage());
  }
}