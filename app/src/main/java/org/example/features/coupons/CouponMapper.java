package org.example.features.coupons;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.example.database.EntityMapper;

public class CouponMapper implements EntityMapper<Coupons> {
  @Override
  public Coupons map(ResultSet rs) throws SQLException {
    String code = rs.getString("code");
    int discount = rs.getInt("discount");
    return new Coupons(code, discount);
  }
}
