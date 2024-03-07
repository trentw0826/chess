package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.databaseAccess.databaseAccessObject.AuthDao;
import dataAccess.databaseAccess.databaseAccessObject.GameDao;
import dataAccess.databaseAccess.databaseAccessObject.UserDao;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import spark.utils.Assert;

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
    var listedData = testUserDao.listData();
    int expectedSize = 0;

    Assertions.assertEquals(expectedSize, listedData.size());
  }

  @Test
  void ListDataOneUserTest() {
    try {
      testUserDao.create(TEST_USER_1);
    } catch (DataAccessException e) {
      throw new RuntimeException(e);
    }

    var listedData = testUserDao.listData();
    int expectedSize = 1;

    Assertions.assertEquals(expectedSize, listedData.size());
    Assertions.assertEquals(TEST_USER_1, listedData.iterator().next());
  }

  @Test
  void createOneUserTest() {
    try {
      testUserDao.create(TEST_USER_1);
    }
    catch (DataAccessException e) {
      throw new RuntimeException(e);
    }

    var listedData = testUserDao.listData();
    int expectedSize = 1;
    Assertions.assertEquals(expectedSize, listedData.size());

    UserData actualUser = testUserDao.listData().iterator().next();
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
    try {
      testUserDao.create(TEST_USER_1);
      testUserDao.create(TEST_USER_2);
      testUserDao.clear();
    }
    catch (DataAccessException e) {
      throw new RuntimeException(e);
    }

    int expectedSize = 0;
    Assertions.assertEquals(expectedSize, testUserDao.listData().size());
  }

  @Test
  void attemptPassword() {
  }
}