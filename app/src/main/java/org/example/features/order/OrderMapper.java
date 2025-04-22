package org.example.features.order;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.example.database.EntityMapper;
import org.example.features.product.Product;

/** The type Order mapper. */
public class OrderMapper implements EntityMapper<Order> {

  @Override
  public Order map(ResultSet rs) throws SQLException {
    List<ProductQuantity> productQuantities = new ArrayList<>();
    int orderId = -1;

    while (rs.next()) {
      if (orderId == -1) {
        orderId = rs.getInt("order_id");
      }

      Product product =
          new Product(
              rs.getInt("product_id"),
              rs.getString("name"),
              rs.getString("description"),
              rs.getDouble("price"),
              rs.getString("image_url"));
      int quantity = rs.getInt("quantity");
      productQuantities.add(new ProductQuantity(product, quantity));
    }

    if (productQuantities.isEmpty()) {
      return null;
    }

    return new Order(orderId, productQuantities);
  }
}
