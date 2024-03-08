package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.databaseAccess.sqlAccessObject.AuthSqlDao;
import dataAccess.databaseAccess.sqlAccessObject.GameSqlDao;
import dataAccess.databaseAccess.sqlAccessObject.UserSqlDao;
import model.GameData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

class GameSqlDaoTest extends SqlaoTest {

  static UserSqlDao testUserSqlDao;
  static AuthSqlDao testAuthSqlDao;
  static GameSqlDao testGameSqlDao;


  @BeforeEach
  void setUp() {
    try {
      testUserSqlDao = new UserSqlDao();
      testAuthSqlDao = new AuthSqlDao();
      testGameSqlDao = new GameSqlDao();

      testUserSqlDao.clear();
      testAuthSqlDao.clear();
      testGameSqlDao.clear();
    }
    catch (DataAccessException e) {
      throw new RuntimeException("Data access objects could not be initialized for testing");
    }
  }

  @AfterEach
  void tearDown() {
    try {
      testUserSqlDao.clear();
      testAuthSqlDao.clear();
      testGameSqlDao.clear();
    }
    catch (DataAccessException e) {
      throw new RuntimeException("Data access objects could not be initialized for testing");
    }
  }

  @Test
  void listDataEmptyTest() {
    Collection<GameData> listedData;
    try {
      listedData = testGameSqlDao.list();
    } catch (DataAccessException e) {
      throw new RuntimeException(e);
    }

    int expectedSize = 0;
    Assertions.assertEquals(expectedSize, listedData.size());
  }

  @Test
  void ListDataOneGameTest() {
    Collection<GameData> listedData;
    try {
      testGameSqlDao.create(TEST_GAME_1);
      listedData = testGameSqlDao.list();
    } catch (DataAccessException e) {
      throw new RuntimeException(e);
    }

    int expectedSize = 1;

    Assertions.assertEquals(expectedSize, listedData.size());
    Assertions.assertEquals(TEST_GAME_1.getGameName(), listedData.iterator().next().getGameName());
  }

  @Test
  void createOneGameTest() {
    Collection<GameData> listedData;
    GameData actualGame;

    try {
      testGameSqlDao.create(TEST_GAME_1);
      listedData = testGameSqlDao.list();
      actualGame = testGameSqlDao.list().iterator().next();
    }

    catch (DataAccessException e) {
      throw new RuntimeException(e);
    }

    int expectedSize = 1;
    Assertions.assertEquals(expectedSize, listedData.size());

    Assertions.assertEquals(TEST_GAME_1.getGameName(), actualGame.getGameName());
  }


  @Test
  void getExistingGameTest() {
    GameData actualRetrievedGame;
    try {
      testGameSqlDao.create(TEST_GAME_1);
      actualRetrievedGame = testGameSqlDao.get(1);
    }
    catch (DataAccessException e) {
      throw new RuntimeException(e);
    }

    Assertions.assertEquals(TEST_GAME_1.getGameName(), actualRetrievedGame.getGameName());
  }


  @Test
  void attemptGetNonExistingGameTest() {
    Throwable actualException = Assertions.assertThrows(DataAccessException.class, () -> {
      testGameSqlDao.get(-1);});
    Assertions.assertEquals(DataAccessException.class, actualException.getClass());
  }

  @Test
  void deleteExistingGameTest() {
    GameData actualRetrievedGame;
    try {
      testGameSqlDao.create(TEST_GAME_1);
      actualRetrievedGame = testGameSqlDao.get(1);
    }
    catch (DataAccessException e) {
      throw new RuntimeException(e);
    }

    Assertions.assertEquals(TEST_GAME_1.getGameName(), actualRetrievedGame.getGameName());
  }

  @Test
  void deleteNonExistingGameTest() {
    Throwable actualException = Assertions.assertThrows(DataAccessException.class, () -> {
      testGameSqlDao.delete(-1);});
    Assertions.assertEquals(DataAccessException.class, actualException.getClass());
  }

  @Test
  void clearTest() {
    Collection<GameData> listedData;

    try {
      testGameSqlDao.create(TEST_GAME_1);
      testGameSqlDao.create(TEST_GAME_2);
      testGameSqlDao.clear();
      listedData = testGameSqlDao.list();
    }
    catch (DataAccessException e) {
      throw new RuntimeException(e);
    }

    int expectedSize = 0;
    Assertions.assertEquals(expectedSize, listedData.size());
  }
}