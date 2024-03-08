package dataAccess;

import model.DataModel;

import java.util.Collection;

/**
 * Defines the functionality of a data access object.
 *
 * @param <T> data model type
 * @param <K> key type associated with the data model
 */
public interface DataAccessObject<K, T extends DataModel<K>> {
  K create(final T data) throws DataAccessException;
  T get(final K key) throws DataAccessException;
  Collection<T> list() throws DataAccessException;
  void delete(final K key) throws DataAccessException;
  void clear() throws DataAccessException;
}
