package model;


public interface DataModel {

  /**
   * @return if all the fields of the implementing class are null
   */
  boolean hasNullFields();
}
