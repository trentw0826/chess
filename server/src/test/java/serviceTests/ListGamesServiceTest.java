package serviceTests;

import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.CreateGameService;
import service.ListGamesService;
import service.RegisterService;
import service.request.CreateGameRequest;
import service.request.ListGamesRequest;
import service.request.RegisterRequest;
import service.response.ListGamesResponse;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;
import static serviceTests.ServiceTestHelper.*;

class ListGamesServiceTest {
  
  static ListGamesService testListGamesService;
  ListGamesRequest testListGamesRequest;
  ListGamesResponse expectedResponse;
  ListGamesResponse actualResponse;
  String existingAuthToken;

  @BeforeAll
  static void beforeAll() {
    testListGamesService = new ListGamesService();
    testListGamesService.clear();
  }

  @BeforeEach
  void setUp() {
    testListGamesService = new ListGamesService();
    testListGamesService.clear();
    testListGamesRequest = null;
    expectedResponse = null;
    actualResponse = null;

    // register user and grab their auth token
    RegisterService testRegisterService = new RegisterService();
    existingAuthToken = testRegisterService.processHandlerRequest(new RegisterRequest(USERNAME1, PASSWORD1, EMAIL1)).getAuthToken();
  }

  @Test
  void listEmptyGamesTest() {
    testListGamesRequest = new ListGamesRequest(existingAuthToken);
    expectedResponse = new ListGamesResponse(new HashSet<>());

    actualResponse = testListGamesService.processHandlerRequest(testListGamesRequest);

    Assertions.assertEquals(expectedResponse.getGames().size(), actualResponse.getGames().size());
  }


  @Test
  void invalidTokenListGamesTest() {
    testListGamesRequest = new ListGamesRequest("invalidtoken123");
    expectedResponse = new ListGamesResponse("Error: unauthorized");

    actualResponse = testListGamesService.processHandlerRequest(testListGamesRequest);

    Assertions.assertEquals(expectedResponse.getMessage(), actualResponse.getMessage());
  }


  @Test
  void listOneGameTest() {
    CreateGameRequest testCreateGameRequest = new CreateGameRequest("someGameName", existingAuthToken);
    CreateGameService testCreateGameService = new CreateGameService();
    testCreateGameService.processHandlerRequest(testCreateGameRequest);

    Collection<GameData> expectedGameList = new HashSet<>();
    expectedGameList.add(new GameData("someGameName"));
    expectedResponse = new ListGamesResponse(expectedGameList);

    testListGamesRequest = new ListGamesRequest(existingAuthToken);

    testListGamesService.processHandlerRequest(testListGamesRequest);
    Collection<GameData> actualGameList = testListGamesService.processHandlerRequest(testListGamesRequest).getGames();

    Iterator<GameData> itExpected = expectedGameList.iterator();
    Iterator<GameData> itActual = actualGameList.iterator();


    Assertions.assertEquals(itExpected.next().getGameName(), itActual.next().getGameName());
  }
}