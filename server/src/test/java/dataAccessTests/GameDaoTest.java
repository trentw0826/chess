package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.databaseAccess.databaseAccessObject.AuthDao;
import dataAccess.databaseAccess.databaseAccessObject.GameDao;
import dataAccess.databaseAccess.databaseAccessObject.UserDao;
import model.GameData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class GameDaoTest extends DaoTest {

  static UserDao testUserDao;
  static AuthDao testAuthDao;
  static GameDao testGameDao;


  @BeforeEach
  void setUp() {
    try {
      testUserDao = new UserDao();
      testAuthDao = new AuthDao();
      testGameDao = new GameDao();

      testUserDao.clear();
      testAuthDao.clear();
      testGameDao.clear();
    }
    catch (DataAccessException e) {
      throw new RuntimeException("Data access objects could not be initialized for testing");
    }
  }

  @AfterEach
  void tearDown() {
    try {
      testUserDao.clear();
      testAuthDao.clear();
      testGameDao.clear();
    }
    catch (DataAccessException e) {
      throw new RuntimeException("Data access objects could not be initialized for testing");
    }
  }

  @Test
  void listDataEmptyTest() {
    Collection<GameData> listedData;
    try {
      listedData = testGameDao.listData();
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
      testGameDao.create(TEST_GAME_1);
      listedData = testGameDao.listData();
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
      testGameDao.create(TEST_GAME_1);
      listedData = testGameDao.listData();
      actualGame = testGameDao.listData().iterator().next();
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
      testGameDao.create(TEST_GAME_1);
      actualRetrievedGame = testGameDao.get(1);
    }
    catch (DataAccessException e) {
      throw new RuntimeException(e);
    }

    Assertions.assertEquals(TEST_GAME_1.getGameName(), actualRetrievedGame.getGameName());
  }


  @Test
  void attemptGetNonExistingGameTest() {
    Throwable actualException = Assertions.assertThrows(DataAccessException.class, () -> {testGameDao.get(-1);});
    Assertions.assertEquals(DataAccessException.class, actualException.getClass());
  }

  @Test
  void deleteExistingGameTest() {
    GameData actualRetrievedGame;
    try {
      testGameDao.create(TEST_GAME_1);
      actualRetrievedGame = testGameDao.get(1);
    }
    catch (DataAccessException e) {
      throw new RuntimeException(e);
    }

    Assertions.assertEquals(TEST_GAME_1.getGameName(), actualRetrievedGame.getGameName());
  }

  @Test
  void deleteNonExistingGameTest() {
    Throwable actualException = Assertions.assertThrows(DataAccessException.class, () -> {testGameDao.delete(-1);});
    Assertions.assertEquals(DataAccessException.class, actualException.getClass());
  }

  @Test
  void clearTest() {
    Collection<GameData> listedData;

    try {
      testGameDao.create(TEST_GAME_1);
      testGameDao.create(TEST_GAME_2);
      testGameDao.clear();
      listedData = testGameDao.listData();
    }
    catch (DataAccessException e) {
      throw new RuntimeException(e);
    }

    int expectedSize = 0;
    Assertions.assertEquals(expectedSize, listedData.size());
  }
}