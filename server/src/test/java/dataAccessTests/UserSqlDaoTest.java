package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.sqlAccess.sqlAccessObjects.AuthSqlDao;
import dataAccess.sqlAccess.sqlAccessObjects.GameSqlDao;
import dataAccess.sqlAccess.sqlAccessObjects.UserSqlDao;
import model.UserData;
import org.junit.jupiter.api.*;

import java.util.Collection;

class UserSqlDaoTest extends SqlaoTest {

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
    Collection<UserData> listedData;
    try {
      listedData = testUserSqlDao.list();
    } catch (DataAccessException e) {
      throw new RuntimeException(e);
    }

    int expectedSize = 0;
    Assertions.assertEquals(expectedSize, listedData.size());
  }

  @Test
  void ListDataOneUserTest() {
    Collection<UserData> listedData;
    try {
      testUserSqlDao.create(TEST_USER_1);
        listedData = testUserSqlDao.list();
    } catch (DataAccessException e) {
      throw new RuntimeException(e);
    }

    int expectedSize = 1;

    Assertions.assertEquals(expectedSize, listedData.size());
    Assertions.assertEquals(TEST_USER_1, listedData.iterator().next());
  }

  @Test
  void createOneUserTest() {
    Collection<UserData> listedData;
    UserData actualUser;

    try {
      testUserSqlDao.create(TEST_USER_1);
      listedData = testUserSqlDao.list();
      actualUser = testUserSqlDao.list().iterator().next();
    }

    catch (DataAccessException e) {
      throw new RuntimeException(e);
    }

    int expectedSize = 1;
    Assertions.assertEquals(expectedSize, listedData.size());

    Assertions.assertEquals(TEST_USER_1, actualUser);
  }

  @Test
  void createAlreadyExistingUserTest() {
    try {
      testUserSqlDao.create(TEST_USER_1);
    }
    catch (DataAccessException e) {
      throw new RuntimeException(e);
    }

    Throwable actualException = Assertions.assertThrows(DataAccessException.class, () -> {
      testUserSqlDao.create(TEST_USER_1);});
    Assertions.assertEquals(DataAccessException.class, actualException.getClass());
  }

  @Test
  void getExistingUserTest() {
    UserData actualRetrievedUser;
    try {
      testUserSqlDao.create(TEST_USER_1);
      actualRetrievedUser = testUserSqlDao.get(TEST_USER_1.username());
    }
    catch (DataAccessException e) {
      throw new RuntimeException(e);
    }

    Assertions.assertEquals(TEST_USER_1, actualRetrievedUser);
  }


  @Test
  void attemptGetNonExistingUserTest() {
    Throwable actualException = Assertions.assertThrows(DataAccessException.class, () -> {
      testUserSqlDao.get(TEST_USERNAME_1);});
    Assertions.assertEquals(DataAccessException.class, actualException.getClass());
  }

  @Test
  void deleteExistingUserTest() {
    UserData actualRetrievedUser;
    try {
      testUserSqlDao.create(TEST_USER_1);
      actualRetrievedUser = testUserSqlDao.get(TEST_USER_1.username());
    }
    catch (DataAccessException e) {
      throw new RuntimeException(e);
    }

    Assertions.assertEquals(TEST_USER_1, actualRetrievedUser);
  }

  @Test
  void deleteNonExistingUserTest() {
    Throwable actualException = Assertions.assertThrows(DataAccessException.class, () -> {
      testUserSqlDao.delete(TEST_USERNAME_1);});
    Assertions.assertEquals(DataAccessException.class, actualException.getClass());
  }

  @Test
  void clearTest() {
    Collection<UserData> listedData;

    try {
      testUserSqlDao.create(TEST_USER_1);
      testUserSqlDao.create(TEST_USER_2);
      testUserSqlDao.clear();
      listedData = testUserSqlDao.list();
    }
    catch (DataAccessException e) {
      throw new RuntimeException(e);
    }

    int expectedSize = 0;
    Assertions.assertEquals(expectedSize, listedData.size());
  }
}