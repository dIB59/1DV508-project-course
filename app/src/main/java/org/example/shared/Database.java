package org.example.shared;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Database {

  private static Database instance;
  private final String URL =
      "jdbc:mysql://localhost/kioske?useSSL=false&allowPublicKeyRetrieval=true";
  private final String USER = "main";
  private final String PASSWORD = "root";
  private Connection connection;

  private Database() {
    try {
      this.connection =
          DriverManager.getConnection(URL + "&user=" + USER + "&password=" + PASSWORD);
    } catch (SQLException e) {
      System.err.println("Connection failed: " + e.getMessage());
    }
  }

  public static synchronized Database getInstance() {
    if (instance == null) {
      instance = new Database();
    }
    return instance;
  }

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
