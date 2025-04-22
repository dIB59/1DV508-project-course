package org.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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

  /**
   * Resets all tables in the database by truncating them.
   */
  public void resetDatabase() {
    List<String> tableNames = getAllTableNames();

    try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {

      stmt.execute("SET FOREIGN_KEY_CHECKS = 0"); // Disable FK checks

      for (String tableName : tableNames) {
        if (!isSystemTable(tableName)) {
          String truncateStatement = "TRUNCATE TABLE " + tableName;
          stmt.executeUpdate(truncateStatement);
          System.out.println("Table '" + tableName + "' truncated.");
        }
      }

      stmt.execute("SET FOREIGN_KEY_CHECKS = 1"); // Re-enable FK checks

    } catch (SQLException e) {
      System.err.println("Failed to reset database: " + e.getMessage());
    }
  }

    /**
     * Retrieves a list of all table names in the current database.
     *
     * @return A list of table names.
     */
    private List<String> getAllTableNames() {
      List<String> tableNames = new ArrayList<>();
      try (Connection conn = getConnection();
           Statement stmt = conn.createStatement();
           ResultSet rs = stmt.executeQuery("SHOW TABLES")) {
        while (rs.next()) {
          tableNames.add(rs.getString(1)); // The first column contains the table name
        }
      } catch (SQLException e) {
        System.err.println("Error retrieving table names: " + e.getMessage());
      }
      return tableNames;
    }

    /**
     * Simple check to identify and exclude common system tables (you might need to adjust this
     * based on your specific database system).
     *
     * @param tableName The name of the table.
     * @return True if the table name suggests it's a system table, false otherwise.
     */
    private boolean isSystemTable(String tableName) {
      return tableName.startsWith("sys") || tableName.startsWith("information_schema") || tableName.startsWith("performance_schema");
      // Add other prefixes or specific table names if needed
    }


}
