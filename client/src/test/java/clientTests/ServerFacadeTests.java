import exception.ResponseException;
import model.GameData;
import org.junit.jupiter.api.*;
import response.ListGamesResponse;
import server.Server;
import ui.ServerFacade;

import java.util.Collection;

class ServerFacadeTests {

  private static Server server;
  private static ServerFacade facade;

  private String validAuth;

  @BeforeAll
  public static void init() {
    server = new Server();
    var port = server.run(0);
    System.out.println("Started test HTTP server on " + port);

    facade = new ServerFacade("http://localhost:8080");
  }

  @AfterAll
  static void stopServer() throws ResponseException {
    facade.clear();
    server.stop();
  }

  @BeforeEach
  public void setup() throws ResponseException {
    facade.clear();
    facade.registerUser("a", "a", "a");
    validAuth = facade.login("a", "a").getAuthToken();
  }

  @Test
  void clearTest() throws ResponseException {
    facade.clear();
    Assertions.assertTrue(true);
  }

  @Test
  void registerValidUser() throws ResponseException {
    facade.registerUser("b", "a", "a");
    Assertions.assertTrue(true);
  }

  @Test
  void registerExistingUser() throws ResponseException {
    Assertions.assertThrows(ResponseException.class,  () -> facade.registerUser("a", "a", "a"));
  }

  @Test
  void validLogin() throws ResponseException {
    facade.registerUser("b", "a", "a");
    facade.login("b", "a");
    Assertions.assertTrue(true);
  }

  @Test
  void nonExistingLogin() throws ResponseException {
    Assertions.assertThrows(ResponseException.class, () -> facade.login("c", "a"));
  }

  @Test
  void badPasswordLogin() throws ResponseException {
    Assertions.assertThrows(ResponseException.class, () -> facade.login("a", "badpassword"));
  }

  @Test
  void createGameTest() throws ResponseException {
    facade.createGame("someGameName", validAuth);
    Assertions.assertTrue(true);
  }

  @Test
  void createExistingGameTest() throws ResponseException {
    facade.createGame("someGameName", validAuth);
    Assertions.assertThrows(ResponseException.class, () -> facade.createGame("someGameName", validAuth));
  }

  @Test
  void listGamesTest() throws ResponseException {
    facade.createGame("someGameName", validAuth);
    Collection<GameData> gamesList = facade.listGames(validAuth).getGames();
    Assertions.assertEquals(1, gamesList.size());
    Assertions.assertEquals("someGameName", gamesList.iterator().next().getGameName());
  }

  @Test
  void listGamesBadAuth() throws ResponseException {
    facade.createGame("someGameName", validAuth);
    Assertions.assertThrows(ResponseException.class, () -> facade.listGames("bad auth"));
  }

  @Test
  void validLogoutTest() throws ResponseException {
    facade.logout(validAuth);
    Assertions.assertTrue(true);
  }

  @Test
  void nonExistingLogoutTest() throws ResponseException {
    Assertions.assertThrows(ResponseException.class, () -> facade.logout("someinvalidauth"));
  }
}