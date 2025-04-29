package org.example.features.coupons;

import java.sql.*;
import org.example.database.Database;

public class CouponsRepository {

  private final Connection connection = Database.getInstance().getConnection();
  public double findByCode(String code)  {
    String sql = "SELECT Coupons.discount FROM Coupons WHERE Coupons.code = ?";

    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
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
}
