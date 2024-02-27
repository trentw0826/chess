package dataAccess.memoryAccess;

import dataAccess.DataAccessException;
import dataAccess.DataAccessObject;
import service.ServiceConstants;

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
public abstract class MemoryAccessObject<K, T> implements DataAccessObject<K, T> {

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
    K key = generateKey(data);
    if (dataExists(key)) {
      throw new DataAccessException(ServiceConstants.ERROR_MESSAGES.ALREADY_TAKEN);
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
      // TODO should throw bad request?
      throw new DataAccessException(ServiceConstants.ERROR_MESSAGES.BAD_REQUEST);
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
      throw new DataAccessException(ServiceConstants.ERROR_MESSAGES.UNAUTHORIZED);
    }
  }


  /**
   * Lists all existing data.
   *
   * @return  collection of data in map.
   */
  @Override
  public Collection<T> listData() {
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

  /**
   * Generates a key based on a data model.
   *
   * @param data  data model
   * @return      the key associated with given data model
   */
  protected abstract K generateKey(T data);
}
