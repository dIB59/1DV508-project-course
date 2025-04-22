package org.example.features.product;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.example.shared.EntityMapper;

/** The type Product mapper. */
public class ProductMapper implements EntityMapper<Product> {

  @Override
  public Product map(ResultSet rs) throws SQLException {
    return new Product(
        rs.getInt("id"),
        rs.getString("name"),
        rs.getString("description"),
        rs.getDouble("price"),
        rs.getString("image_url"));
  }
}
