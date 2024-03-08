package dataAccess.memoryAccess;

import dataAccess.DataAccessException;
import dataAccess.DataAccessObject;
import model.DataModel;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


/**
 * Implements the functionality of a local, memory-based data access object.
 * Holds local data in a map.
 *
 * @param <T> data model type
 * @param <K> key type associated with data model type
 */
public abstract class MemoryAccessObject<K, T extends DataModel<K>> implements DataAccessObject<K, T> {

  /* Local data structure holding data values*/
  protected final Map<K, T> localData = new HashMap<>();


  /**
   * Creates a new data entry.
   *
   * @param data data to be inserted
   * @throws DataAccessException if data already exists
   */
  @Override
  public K create(T data) throws DataAccessException {
    K key = data.generateKey();
    if (dataExists(key)) {
      throw new DataAccessException(DataAccessException.ErrorMessages.ALREADY_TAKEN);
    }
    else {
      localData.put(key, data);
      return key;
    }
  }


  /**
   * Retrieves a data entry.
   *
   * @param key key to retrieve data at
   * @return    data found at the given key
   * @throws DataAccessException  if no data is found at the given key
   */
  @Override
  public T get(K key) throws DataAccessException {
    if (dataExists(key)) {
      return localData.get(key);
    }
    else {
      throw new DataAccessException(DataAccessException.ErrorMessages.BAD_REQUEST);
    }
  }


  /**
   * Deletes a data entry.
   *
   * @param key key at which to delete data
   * @throws DataAccessException  if data doesn't exist
   */
  @Override
  public void delete(K key) throws DataAccessException {
    if (dataExists(key)) {
      localData.remove(key);
    }
    else {
      throw new DataAccessException(DataAccessException.ErrorMessages.UNAUTHORIZED);
    }
  }


  /**
   * Lists all existing data.
   *
   * @return  collection of data in map.
   */
  @Override
  public Collection<T> list() {
    return localData.values();
  }


  /**
   * Clears all existing data.
   */
  @Override
  public void clear() {
    localData.clear();
  }


  /**
   * Checks if data entry exists.
   *
   * @param key key to check for existence
   * @return    if 'key' exists in the local data
   */
  public boolean dataExists(K key) {
    return localData.containsKey(key);
  }
}
