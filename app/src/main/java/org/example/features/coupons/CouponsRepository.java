package org.example.features.coupons;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.example.database.CrudRepository;
import org.example.database.EntityMapper;

public class CouponsRepository implements CrudRepository<Coupons, String> {

  private final Connection connection;
  private final EntityMapper<Coupons> couponMapper = new CouponMapper();

  public CouponsRepository(Connection connection) {
    this.connection = connection;
  }

  public double findDiscountByCode(String code) {
    String sql = "SELECT Coupons.discount FROM Coupons WHERE Coupons.code = ?";

    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setString(1, code);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        return rs.getDouble("discount");
      }
    } catch (SQLException e) {
      System.out.println("Coupons could not be found: " + e.getMessage());
    }
    return 0;
  }

  public Coupons save(Coupons entity) throws SQLException {
    String sql = "INSERT INTO Coupons (CODE, DISCOUNT) VALUES (?, ?)";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setString(1, entity.getCode());
      stmt.setDouble(2, entity.getDiscount());
      stmt.executeUpdate();
    }
    return entity;
  }

  @Override
  public Optional<Coupons> findById(String code) throws SQLException {
    String sql = "SELECT * FROM Coupons WHERE Coupons.code = ?";

    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setString(1, code);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        Coupons coupon = couponMapper.map(rs);
        return Optional.of(coupon);
      }
    } catch (SQLException e) {
      System.out.println("Coupons could not be found: " + e.getMessage());
    }
    return Optional.empty();
  }

  public void update(Coupons entity) throws SQLException {
    String sql = "UPDATE Coupons SET discount = ? WHERE code = ?";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setString(1, entity.getCode());
      stmt.setDouble(2, entity.getDiscount());
      stmt.executeUpdate();
    }
  }

  public void delete(String code) throws SQLException {
    String sql = "DELETE FROM Coupons WHERE code = ?";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setString(1, code);
      stmt.executeUpdate();
    }
  }

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
