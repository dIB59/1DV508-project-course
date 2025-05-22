package org.example.features.order;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.example.database.EntityMapper;
import org.example.features.ingredients.Ingredient;
import org.example.features.product.CustomizedProduct;
import org.example.features.product.Product;
import org.example.features.product.Tag;

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

      List<Tag> tags = new ArrayList<>();
      if (rs.getArray("tags") != null && rs.getArray("tag_ids") != null) {
        String[] tagNames = (String[]) rs.getArray("tags").getArray();
        Integer[] tagIds = (Integer[]) rs.getArray("tag_ids").getArray();

        for (int i = 0; i < tagNames.length; i++) {
          tags.add(new Tag(tagIds[i], tagNames[i]));
        }
      }

      Product product =
          new Product(
              rs.getInt("product_id"),
              rs.getString("name"),
              rs.getString("description"),
              rs.getDouble("price"),
              rs.getString("image_url"),
              rs.getString("specialLabel"),
              rs.getBoolean("isASide"),
              tags
          );
        Map<Ingredient, Integer> ingredientQuantities = new HashMap<>();
      CustomizedProduct customizedProduct = new CustomizedProduct(product, ingredientQuantities);
      int quantity = rs.getInt("quantity");
      productQuantities.add(new ProductQuantity(customizedProduct, quantity));
    }

    if (productQuantities.isEmpty()) {
      return null;
    }

    return new Order(orderId, productQuantities);
  }
}
