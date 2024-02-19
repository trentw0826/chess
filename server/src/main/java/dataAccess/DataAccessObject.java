package dataAccess;

public interface DataAccessObject {
  /**
   * Clear all data associated with the data access object.
   *
   * @throws DataAccessException  data could not be cleared
   */
  public void clearData() throws DataAccessException;
}
