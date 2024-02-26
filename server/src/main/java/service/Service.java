package service;


import dataAccess.memoryAccess.memoryAccessObject.*;

import java.util.UUID;

public abstract class Service {
  /* local databases */
  protected static final UserMAO memoryUserDAO = new UserMAO();
  protected static final AuthMAO memoryAuthDAO = new AuthMAO();
  protected static final GameMAO memoryGameDAO = new GameMAO();

  public static String generateNewAuthToken() {
    return UUID.randomUUID().toString();
  }

  Service() {}
}
