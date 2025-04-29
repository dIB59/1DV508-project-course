package org.example.features.coupons;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.swing.plaf.ScrollBarUI;
import org.example.database.CrudRepository;
import org.example.database.Database;
import org.example.database.EntityMapper;
import org.example.features.admin.Admin;

public class CouponsRepository implements CrudRepository<Coupons> {

  private final Connection connection = Database.getInstance().getConnection();
  private final CouponMapper couponMapper = new CouponMapper();

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

  @Override
  public Coupons save(Coupons entity) throws SQLException {
    return null;
  }

  @Override
  public Optional<Coupons> findById(int id) throws SQLException {
    return Optional.empty();
  }

  @Override
  public void update(Coupons entity) throws SQLException {

  }

  @Override
  public void delete(int id) throws SQLException {

  }

  @Override
  public List<Coupons> findAll() throws SQLException {
    String sql = "SELECT * FROM Coupons";
    try (PreparedStatement stmt = connection.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {
      List<Coupons> coupons = new ArrayList<>();
      while (rs.next()) {
        coupons.add(couponMapper.map(rs));
      }
      return coupons;
    }
  }
}
