package model;


public interface DataModel<K> {

  /**
   * @return if all the fields of the implementing class are null
   */
  boolean hasNullFields();

  /**
   * @return the unique identifier key defined by the inheriting class
   */
  public K generateKey();
}
