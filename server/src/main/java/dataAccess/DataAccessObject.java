package dataAccess;

import model.DataModel;

import java.util.Collection;

/**
 * Defines the functionality of a data access object.
 *
 * @param <T> data model type
 * @param <K> key type associated with the data model
 */
// TODO Should all data access objects be static utility classes?
public interface DataAccessObject<K, T extends DataModel<K>> {
  K create(T data) throws DataAccessException;
  T get(K key) throws DataAccessException;
  Collection<T> listData() throws DataAccessException;
  void delete(K key) throws DataAccessException;
  void clear() throws DataAccessException;
}
