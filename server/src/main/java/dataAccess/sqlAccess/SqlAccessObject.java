package dataAccess.sqlAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.DataAccessObject;
import model.DataModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public abstract class SqlAccessObject<K, T extends DataModel<K>> implements DataAccessObject<K, T> {

  protected static final Gson gson = new Gson();
  protected static Connection connection = null;

  protected SqlAccessObject() {
    try {
      configureDatabase();
    }
    catch (DataAccessException e) {
      throw new IllegalStateException("Database couldn't be initialized: " + e.getMessage());
    }
  }

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
                game JSON,
                gameActive BOOLEAN NOT NULL
            )
            """,
          """
          CREATE TABLE IF NOT EXISTS observers (
              id INT AUTO_INCREMENT PRIMARY KEY,
              username VARCHAR(255) NOT NULL,
              gameID INT NOT NULL,
              
              FOREIGN KEY (gameID) REFERENCES gamedata(gameID)
          )
          """
  };


  /**
   * Configure database upon startup with pre-defined create statements.
   *
   * @throws DataAccessException  if sql error thrown during configuration
   */
  private static void configureDatabase() throws DataAccessException {
    DatabaseManager.createDatabase();
    try {
      for (var statement : CREATE_STATEMENTS) {
        executeUpdate(statement);
      }
    }
    catch (SQLException e) {
      throw new RuntimeException("Unable to configure database: " + e.getMessage());
    }
  }


  /**
   * Executes a SQL query in the form of a string.
   *
   * @param statement       SQL query string that includes wildcards
   * @param statementParams a vararg of parameters to fill the wildcard parameters in the given statement
   * @return                the generated auto key, if applicable ('-1' otherwise)
   * @throws SQLException   if SQL error thrown during query processing
   */
  protected static int executeUpdate(String statement, Object... statementParams) throws SQLException {
    if (connection == null) {
      try {
        connection = DatabaseManager.getConnection();
      } catch (DataAccessException e) {
        throw new IllegalAccessError("Connection couldn't be established with database");
      }
    }
    try (PreparedStatement preparedStatement = connection.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
      for (var i = 0; i < statementParams.length; i++) {
        var param = statementParams[i];
        switch (param) {
          case String p -> preparedStatement.setString(i + 1, p);
          case Integer p -> preparedStatement.setInt(i + 1, p);
          case ChessGame p -> preparedStatement.setString(i + 1, gson.toJson(p));
          case null -> preparedStatement.setNull(i + 1, NULL);
          default -> throw new IllegalArgumentException("Illegal parameter include in SQL statement");
        }
      }
      preparedStatement.executeUpdate();

      var rs = preparedStatement.getGeneratedKeys();

      if (rs.next()) {
        return rs.getInt(1);
      }

      return -1;
    }
  }
}
