package org.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton class to manage the database connection.
 *
 * <p>This class provides a method to get a single instance of the database connection.
 *
 * <p>Example usage:
 *
 * <pre>
 * Connection conn = Database.getInstance().getConnection();
 * </pre>
 */
public class Database {

  private static Database instance;
  private final String URL =
      "jdbc:mysql://localhost:3307/kioske?useSSL=false&allowPublicKeyRetrieval=true";
  private final String USER = "root";
  private final String PASSWORD = "root";

  private Connection connection;

  protected Database() {}

  /**
   * Gets instance.
   *
   * @return the instance
   */
  public static Database getInstance() {
    if (instance == null) {
      instance = new Database();
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
