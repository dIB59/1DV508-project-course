package org.example.features.order;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.example.database.EntityMapper;
import org.example.features.coupons.Coupons;
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

    Coupons discount = new Coupons("No Discount", 0);
    Optional<Integer> memberId = Optional.empty();
    Order.Type type = Order.Type.EAT_IN; // Default value

    int feedback = 0;
    boolean isReceipt = false;
    boolean isPaid = false;
    LocalDateTime createdAt = null;
    while (rs.next()) {
      if (orderId == -1) {
        orderId = rs.getInt("order_id");

        feedback = rs.getInt("feedback");

        // Get discount (coupon)
        String couponCode = rs.getString("coupon_code");
        int couponDiscount = rs.getInt("discount"); // ensure 'discount' is selected in query
        if (couponCode != null) {
          discount = new Coupons(couponCode, couponDiscount);
        }

        // Get optional member
        int memberRaw = rs.getInt("member_id");
        if (!rs.wasNull()) {
          memberId = Optional.of(memberRaw);
        }

        isReceipt = rs.getBoolean("is_receipt");
        isPaid = rs.getBoolean("is_paid");
        String typeStr = rs.getString("type");
        if (typeStr != null) {
          type = Order.Type.valueOf(typeStr);
        }

        createdAt = rs.getTimestamp("created_at").toLocalDateTime();
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

    return new Order(
        orderId,
        productQuantities,
        Optional.of(discount),
        feedback,
        memberId,
        isReceipt,
        type,
        isPaid,
        createdAt
    );

  }
}
