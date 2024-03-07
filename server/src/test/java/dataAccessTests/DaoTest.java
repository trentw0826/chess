package dataAccessTests;

import model.UserData;

public class DaoTest {
  protected static final String TEST_USERNAME_1 = "SomeUsername1";
  protected static final String TEST_USERNAME_2 = "SomeUsername2";
  protected static final String TEST_USERNAME_3 = "SomeUsername3";

  protected static final String TEST_PASSWORD_1 = "somepassword1";
  protected static final String TEST_PASSWORD_2 = "somepassword2";
  protected static final String TEST_PASSWORD_3 = "somepassword3";

  protected static final String TEST_EMAIL_1 = "some.email@1";
  protected static final String TEST_EMAIL_2 = "some.email@2";
  protected static final String TEST_EMAIL_3 = "some.email@3";

  protected static final UserData TEST_USER_1 = new UserData(TEST_USERNAME_1, TEST_PASSWORD_1, TEST_EMAIL_1);
  protected static final UserData TEST_USER_2 = new UserData(TEST_USERNAME_2, TEST_PASSWORD_2, TEST_EMAIL_2);
  protected static final UserData TEST_USER_3 = new UserData(TEST_USERNAME_3, TEST_PASSWORD_3, TEST_EMAIL_3);

  protected static final String GAME_NAME_1 = "Some Game Name 1";
  protected static final String GAME_NAME_2 = "Some Game Name 2";
}
