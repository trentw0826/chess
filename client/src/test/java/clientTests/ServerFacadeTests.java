package clientTests;

import exception.ResponseException;
import model.GameData;
import org.junit.jupiter.api.*;
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

    facade = new ServerFacade("http://localhost:" + port);
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
    Assertions.assertDoesNotThrow(() -> facade.clear());
  }


  @Test
  void registerValidUser() throws ResponseException {
    Assertions.assertDoesNotThrow(() -> facade.registerUser("b", "a", "a"));
  }

  @Test
  void registerExistingUser() throws ResponseException {
    Assertions.assertThrows(ResponseException.class,  () -> facade.registerUser("a", "a", "a"));
  }

  @Test
  void validLogin() throws ResponseException {
    facade.registerUser("b", "a", "a");
    Assertions.assertDoesNotThrow(() -> facade.login("b", "a"));
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
    Assertions.assertDoesNotThrow(() -> facade.createGame("someGameName", validAuth));
  }

  @Test
  void createGameBadAuth() throws ResponseException {
    Assertions.assertThrows(ResponseException.class, () -> facade.createGame("someGameName", "badauth"));
  }

  @Test
  void joinGameTest() throws ResponseException {
    facade.createGame("someGameName", validAuth);
    Assertions.assertDoesNotThrow(() -> facade.joinGame(validAuth, "white", 1));
  }

  @Test
  void joinNonExistingGameTest() {
    Assertions.assertThrows(ResponseException.class, () -> facade.joinGame(validAuth, "white", 1));
  }

  @Test
  void badColorJoinGameTest() throws ResponseException {
    facade.createGame("someGameName", validAuth);
    Assertions.assertThrows(ResponseException.class, () -> facade.joinGame(validAuth, "not a color", 1));
  }

  @Test
  void observeGameTest() throws ResponseException {
    facade.createGame("someGameName", validAuth);
    Assertions.assertDoesNotThrow(() -> facade.joinGame(validAuth, null, 1));
  }

  @Test
  void observeNonExistingGameTest() {
    Assertions.assertThrows(ResponseException.class, () -> facade.joinGame(validAuth, null, 1));
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