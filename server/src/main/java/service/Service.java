package service;


import dataAccess.memoryAccess.memoryAccessObject.*;

public abstract class Service {
  /* local databases */
  protected static final UserMAO memoryUserDAO = new UserMAO();
  protected static final AuthMAO memoryAuthDAO = new AuthMAO();
  protected static final GameMAO memoryGameDAO = new GameMAO();

  Service() {}
}
