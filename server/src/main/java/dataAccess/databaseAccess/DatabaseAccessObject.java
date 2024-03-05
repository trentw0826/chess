package dataAccess.databaseAccess;

import dataAccess.DataAccessException;
import dataAccess.DataAccessObject;
import dataAccess.DatabaseManager;
import model.DataModel;

import java.sql.SQLException;

public abstract class DatabaseAccessObject<K, T extends DataModel> implements DataAccessObject<K, T> {

  /*
   * Initializing statements for the database table creations
   */
  private static final String[] CREATE_STATEMENTS = {
          """
            CREATE TABLE IF NOT EXISTS authdata (
                authToken VARCHAR(255) NOT NULL,
                username VARCHAR(255) NOT NULL,
                PRIMARY KEY (authToken)
            )
            """,
          """
            CREATE TABLE IF NOT EXISTS userdata (
                username VARCHAR(255) NOT NULL,
                password VARCHAR(255) NOT NULL,
                email VARCHAR(255) NOT NULL,
                
                PRIMARY KEY (username)
            )
            """,
          """
            CREATE TABLE IF NOT EXISTS gamedata (
                gameID INT AUTO_INCREMENT PRIMARY KEY,
                whiteUsername VARCHAR(255),
                blackUsername VARCHAR(255),
                gameName VARCHAR(255) NOT NULL,
                game JSON NOT NULL
            )
            """,
          """
          CREATE TABLE observers (
              username VARCHAR(255) NOT NULL,
              gameID INT NOT NULL,
              
              PRIMARY KEY (username),
              FOREIGN KEY (gameID) REFERENCES gamedata(gameID)
          )
          """
  };


  /**
   * Configure database upon startup with pre-defined create statements.
   *
   * @throws DataAccessException  if sql error thrown during configuration
   */
  private void configureDatabase() throws DataAccessException {
    DatabaseManager.createDatabase();
    try (var conn = DatabaseManager.getConnection()) {
      for (var statement : CREATE_STATEMENTS) {
        try (var preparedStatement = conn.prepareStatement(statement)) {
          preparedStatement.executeUpdate();
        }
      }
    }
    catch (SQLException ex) {
      throw new RuntimeException("Unable to configure database: " + ex.getMessage());
    }
  }
}
