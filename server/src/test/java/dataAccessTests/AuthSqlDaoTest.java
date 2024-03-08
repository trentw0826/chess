package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.databaseAccess.sqlAccessObject.AuthSqlDao;
import dataAccess.databaseAccess.sqlAccessObject.GameSqlDao;
import dataAccess.databaseAccess.sqlAccessObject.UserSqlDao;
import model.AuthData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

class AuthSqlDaoTest extends SqlaoTest {

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
  void listEmptyAuthTest() {
    Collection<AuthData> listedData;
    try {
      listedData = testAuthSqlDao.list();
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
      testAuthSqlDao.create(TEST_AUTH_1);
      listedData = testAuthSqlDao.list();
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
      testAuthSqlDao.create(TEST_AUTH_1);
      listedData = testAuthSqlDao.list();
      actualAuth = testAuthSqlDao.list().iterator().next();
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
      testAuthSqlDao.create(TEST_AUTH_1);
    }
    catch (DataAccessException e) {
      throw new RuntimeException(e);
    }

    Throwable actualException = Assertions.assertThrows(DataAccessException.class, () -> {
      testAuthSqlDao.create(TEST_AUTH_1);});
    Assertions.assertEquals(DataAccessException.class, actualException.getClass());
  }

  @Test
  void getExistingAuthTest() {
    AuthData actualRetrievedAuth;
    try {
      testAuthSqlDao.create(TEST_AUTH_1);
      actualRetrievedAuth = testAuthSqlDao.get(TEST_AUTH_1.authToken());
    }
    catch (DataAccessException e) {
      throw new RuntimeException(e);
    }

    Assertions.assertEquals(TEST_AUTH_1, actualRetrievedAuth);
  }

  @Test
  void getNonExistingAuthTest() {
    Throwable actualException = Assertions.assertThrows(DataAccessException.class, () -> {
      testAuthSqlDao.get(TEST_AUTH_TOKEN_1);});
    Assertions.assertEquals(DataAccessException.class, actualException.getClass());
  }

  @Test
  void deleteExistingAuthTest() {
    AuthData actualRetrievedAuth;
    try {
      testAuthSqlDao.create(TEST_AUTH_1);
      actualRetrievedAuth = testAuthSqlDao.get(TEST_AUTH_1.authToken());
    }
    catch (DataAccessException e) {
      throw new RuntimeException(e);
    }

    Assertions.assertEquals(TEST_AUTH_1, actualRetrievedAuth);
  }

  @Test
  void deleteNonExistingAuthTest() {
    Throwable actualException = Assertions.assertThrows(DataAccessException.class, () -> {
      testAuthSqlDao.delete(TEST_AUTH_TOKEN_1);});
    Assertions.assertEquals(DataAccessException.class, actualException.getClass());
  }

  @Test
  void clearTest() {
    Collection<AuthData> listedData;

    try {
      testAuthSqlDao.create(TEST_AUTH_1);
      testAuthSqlDao.create(TEST_AUTH_2);
      testAuthSqlDao.clear();
      listedData = testAuthSqlDao.list();
    }
    catch (DataAccessException e) {
      throw new RuntimeException(e);
    }

    int expectedSize = 0;
    Assertions.assertEquals(expectedSize, listedData.size());
  }
}