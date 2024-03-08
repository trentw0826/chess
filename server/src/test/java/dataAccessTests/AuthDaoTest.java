package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.databaseAccess.databaseAccessObject.AuthDao;
import dataAccess.databaseAccess.databaseAccessObject.GameDao;
import dataAccess.databaseAccess.databaseAccessObject.UserDao;
import model.AuthData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class AuthDaoTest extends DaoTest {

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
  void listEmptyAuthTest() {
    Collection<AuthData> listedData;
    try {
      listedData = testAuthDao.listData();
    } catch (DataAccessException e) {
      throw new RuntimeException(e);
    }

    int expectedSize = 0;
    Assertions.assertEquals(expectedSize, listedData.size());
  }

  @Test
  void listOneAuthTest() {
    Collection<AuthData> listedData;
    try {
      testAuthDao.create(TEST_AUTH_1);
      listedData = testAuthDao.listData();
    } catch (DataAccessException e) {
      throw new RuntimeException(e);
    }

    int expectedSize = 1;

    Assertions.assertEquals(expectedSize, listedData.size());
    Assertions.assertEquals(TEST_AUTH_1, listedData.iterator().next());
  }

  @Test
  void createOneAuthTest() {
    Collection<AuthData> listedData;
    AuthData actualAuth;

    try {
      testAuthDao.create(TEST_AUTH_1);
      listedData = testAuthDao.listData();
      actualAuth = testAuthDao.listData().iterator().next();
    }

    catch (DataAccessException e) {
      throw new RuntimeException(e);
    }

    int expectedSize = 1;
    Assertions.assertEquals(expectedSize, listedData.size());

    Assertions.assertEquals(TEST_AUTH_1, actualAuth);
  }


  @Test
  void createAlreadyExistingAuthTest() {
    try {
      testAuthDao.create(TEST_AUTH_1);
    }
    catch (DataAccessException e) {
      throw new RuntimeException(e);
    }

    Throwable actualException = Assertions.assertThrows(DataAccessException.class, () -> {testAuthDao.create(TEST_AUTH_1);});
    Assertions.assertEquals(DataAccessException.class, actualException.getClass());
  }

  @Test
  void getExistingAuthTest() {
    AuthData actualRetrievedAuth;
    try {
      testAuthDao.create(TEST_AUTH_1);
      actualRetrievedAuth = testAuthDao.get(TEST_AUTH_1.authToken());
    }
    catch (DataAccessException e) {
      throw new RuntimeException(e);
    }

    Assertions.assertEquals(TEST_AUTH_1, actualRetrievedAuth);
  }

  @Test
  void getNonExistingAuthTest() {
    Throwable actualException = Assertions.assertThrows(DataAccessException.class, () -> {testAuthDao.get(TEST_AUTH_TOKEN_1);});
    Assertions.assertEquals(DataAccessException.class, actualException.getClass());
  }

  @Test
  void deleteExistingAuthTest() {
    AuthData actualRetrievedAuth;
    try {
      testAuthDao.create(TEST_AUTH_1);
      actualRetrievedAuth = testAuthDao.get(TEST_AUTH_1.authToken());
    }
    catch (DataAccessException e) {
      throw new RuntimeException(e);
    }

    Assertions.assertEquals(TEST_AUTH_1, actualRetrievedAuth);
  }

  @Test
  void deleteNonExistingAuthTest() {
    Throwable actualException = Assertions.assertThrows(DataAccessException.class, () -> {testAuthDao.delete(TEST_AUTH_TOKEN_1);});
    Assertions.assertEquals(DataAccessException.class, actualException.getClass());
  }

  @Test
  void clearTest() {
    Collection<AuthData> listedData;

    try {
      testAuthDao.create(TEST_AUTH_1);
      testAuthDao.create(TEST_AUTH_2);
      testAuthDao.clear();
      listedData = testAuthDao.listData();
    }
    catch (DataAccessException e) {
      throw new RuntimeException(e);
    }

    int expectedSize = 0;
    Assertions.assertEquals(expectedSize, listedData.size());
  }
}