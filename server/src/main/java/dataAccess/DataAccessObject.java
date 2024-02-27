package dataAccess;

import java.util.Collection;

/**
 * Defines the functionality of a data access object.
 *
 * @param <T> data model type
 * @param <K> key type associated with the data model
 */
public interface DataAccessObject<K, T> {
  K create(T data) throws DataAccessException;
  T get(K key) throws DataAccessException;
//  T update(T newData) throws DataAccessException;
  void delete(K key) throws DataAccessException;
  Collection<T> listData();
  void clear();
}
