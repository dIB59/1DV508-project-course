package org.example.features.order;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.example.shared.EntityMapper;

/**
 * The type Order mapper.
 */
public class OrderMapper implements EntityMapper<Order> {

  @Override
  public Order map(ResultSet rs) throws SQLException {
    var productQuantities = new ArrayList<ProductQuantity>();
    rs.getArray("product_quantity").getArray();
    return new Order(
        rs.getInt("id"),
        productQuantities
    );
  }
}
