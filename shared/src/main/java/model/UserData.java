package model;

/**
 *  Holds data to represent a chess-playing client.
 *
 * @param username  user's username
 * @param password  user's password
 * @param email     user's email
 */
public record UserData(String username, String password, String email) implements DataModel {
  @Override
  public boolean hasNullFields() {
    return (username == null || password == null || email == null);
  }
}