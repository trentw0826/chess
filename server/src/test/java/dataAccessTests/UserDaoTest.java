package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.databaseAccess.databaseAccessObject.AuthDao;
import dataAccess.databaseAccess.databaseAccessObject.GameDao;
import dataAccess.databaseAccess.databaseAccessObject.UserDao;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;

import java.util.Collection;

class UserDaoTest extends DaoTest {

  static UserDao testUserDao;
  static AuthDao testAuthDao;
  static GameDao testGameDao;

  static Server testServer;


  @BeforeAll
  static void init() {

  }

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
  void tearDown() throws DataAccessException {
    testUserDao.clear();
    testAuthDao.clear();
    testGameDao.clear();
  }

  @Test
  void listDataEmptyTest() {
    Collection<UserData> listedData;
    try {
      listedData = testUserDao.listData();
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
      testUserDao.create(TEST_USER_1);
        listedData = testUserDao.listData();
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
      testUserDao.create(TEST_USER_1);
      listedData = testUserDao.listData();
      actualUser = testUserDao.listData().iterator().next();
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
      testUserDao.create(TEST_USER_1);
    }
    catch (DataAccessException e) {
      throw new RuntimeException(e);
    }

    Throwable actualException = Assertions.assertThrows(DataAccessException.class, () -> {testUserDao.create(TEST_USER_1);});
    Assertions.assertEquals(DataAccessException.class, actualException.getClass());
  }

  @Test
  void getExistingUserTest() {
    UserData actualRetrievedUser;
    try {
      testUserDao.create(TEST_USER_1);
      actualRetrievedUser = testUserDao.get(TEST_USER_1.username());
    }
    catch (DataAccessException e) {
      throw new RuntimeException(e);
    }

    Assertions.assertEquals(TEST_USER_1, actualRetrievedUser);
  }


  @Test
  void attemptGetNonExistingUserTest() {
    Throwable actualException = Assertions.assertThrows(DataAccessException.class, () -> {testUserDao.get(TEST_USERNAME_1);});
    Assertions.assertEquals(DataAccessException.class, actualException.getClass());
  }

  @Test
  void deleteExistingUserTest() {
    UserData actualRetrievedUser;
    try {
      testUserDao.create(TEST_USER_1);
      actualRetrievedUser = testUserDao.get(TEST_USER_1.username());
    }
    catch (DataAccessException e) {
      throw new RuntimeException(e);
    }

    Assertions.assertEquals(TEST_USER_1, actualRetrievedUser);
  }

  @Test
  void deleteNonExistingUserTest() {
    Throwable actualException = Assertions.assertThrows(DataAccessException.class, () -> {testUserDao.delete(TEST_USERNAME_1);});
    Assertions.assertEquals(DataAccessException.class, actualException.getClass());
  }

  @Test
  void clearTest() {
    Collection<UserData> listedData;

    try {
      testUserDao.create(TEST_USER_1);
      testUserDao.create(TEST_USER_2);
      testUserDao.clear();
      listedData = testUserDao.listData();
    }
    catch (DataAccessException e) {
      throw new RuntimeException(e);
    }

    int expectedSize = 0;
    Assertions.assertEquals(expectedSize, listedData.size());
  }

  @Test
  void attemptPassword() {
  }
}