package serviceTests;

import java.util.UUID;

public class ServiceTestHelper {
  public static boolean validAuthToken(String input) {
    if (input == null) return false;
    try {
      UUID.fromString(input);
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }

  public static final String USERNAME1 = "someuser1";
  public static final String PASSWORD1 = "somepassword1";
  public static final String EMAIL1 = "someemail1";
  public static final String USERNAME2 = "someuser2";
  public static final String PASSWORD2 = "somepassword2";
  public static final String EMAIL2 = "someemail2";
  public static final String FILLERTEXT = "fillertext123";

}
