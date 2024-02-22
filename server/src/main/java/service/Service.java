package service;

import dataAccess.memoryAccess.*;
import response.ClearResponse;

public class Service {
//  private static Service instance;

  protected static final MemoryUserDAO memoryUserDAO = new MemoryUserDAO();
  protected static final MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();
  protected static final MemoryGameDAO memoryGameDAO = new MemoryGameDAO();

//  public static Service getInstance() {
//    if (instance == null) {
//      instance = new Service();
//    }
//    return instance;
//  }

  public ClearResponse clearAllLocalDatabases() {
    memoryUserDAO.clear();
    memoryAuthDAO.clear();
    memoryGameDAO.clear();

    // Filler clear response object
    return new ClearResponse();
  }
}
