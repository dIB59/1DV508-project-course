package org.example.features.coupons;

import java.sql.*;

public class CouponsRepository {

  private final String URL =
      "jdbc:mysql://localhost/kioske?useSSL=false&allowPublicKeyRetrieval=true";
  private final String USER = "root";
  private final String PASSWORD = "root";
  private Connection connection;

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
  public double findByCode(String code)  {
    String sql = "SELECT Coupons.discount FROM Coupons WHERE Coupons.code = ?";

    try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
      stmt.setString(1, code);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        return rs.getDouble("discount");
      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      System.out.println("Coupons could not be found: " + e.getMessage());;
    }
    return 0;
  }
  public static void main(String[] args) {
    System.out.println(new CouponsRepository().findByCode("code8"));
    System.out.println(new CouponsRepository().findByCode("code10"));




  }
}