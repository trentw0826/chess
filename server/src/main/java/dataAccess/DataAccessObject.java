package dataAccess;

//TODO: Potentially interface to be generic?

public interface DataAccessObject {
  /**
   * Clear all data associated with the data access object.
   *
   * @throws DataAccessException  data could not be cleared
   */
  public void clearData();
}
