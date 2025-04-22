package org.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class TestDatabase extends Database {

  private static Database instance;
  private final String URL =
      "jdbc:mysql://localhost/kioske_test?useSSL=false&allowPublicKeyRetrieval=true";
  private final String USER = "root";
  private final String PASSWORD = "root";
  private Connection connection;

  private TestDatabase() {}

  /**
   * Gets instance.
   *
   * @return the instance
   */
  public static Database getInstance() {
    if (instance == null) {
      instance = new TestDatabase();
    }
    return instance;
  }

  /**
   * Gets connection.
   *
   * @return the connection
   */
  public Connection getConnection() {
    try {
      if (connection == null || connection.isClosed()) {
        connection = DriverManager.getConnection(URL + "&user=" + USER + "&password=" + PASSWORD);
      }
    } catch (SQLException e) {
      System.err.println("Reconnection failed: " + e.getMessage());
    }
    return connection;
  }
}
