package org.example.features.order;

import java.sql.*;
import java.util.*;
import org.example.database.CrudRepository;
import org.example.database.EntityMapper;
import org.example.features.ingredients.Ingredient;

public class OrderRepository implements CrudRepository<Order, Integer> {

  private final Connection connection;
  private final EntityMapper<Order> orderMapper;

  public OrderRepository(Connection connection, EntityMapper<Order> orderMapper) {
    this.connection = connection;
    this.orderMapper = orderMapper;
  }

  public Order save(Order order) throws SQLException {
    String insertOrderSql =
        "INSERT INTO Orders (feedback, coupon_code, is_member, member_id, is_receipt, type, is_paid, created_at) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    try (PreparedStatement orderStmt =
        connection.prepareStatement(insertOrderSql, Statement.RETURN_GENERATED_KEYS)) {
      orderStmt.setInt(1, order.getFeedback());

      if (order.getDiscount() != null && order.getDiscount().isPresent()) {
        orderStmt.setString(2, order.getDiscount().get().getCode());
      } else {
        orderStmt.setNull(2, Types.VARCHAR);
      }

      // is_member can be derived from memberId presence
      orderStmt.setBoolean(3, order.getMemberId().isPresent());

      if (order.getMemberId().isPresent()) {
        orderStmt.setInt(4, order.getMemberId().get());
      } else {
        orderStmt.setNull(4, Types.INTEGER);
      }

      orderStmt.setBoolean(5, order.isReceipt());

      // store enum as string
      orderStmt.setString(6, order.getType().name());

      orderStmt.setBoolean(7, order.isPaid());

      // Use Timestamp for LocalDateTime
      orderStmt.setTimestamp(8, Timestamp.valueOf(order.getCreatedAt()));

      orderStmt.executeUpdate();
      ResultSet rs = orderStmt.getGeneratedKeys();
      if (rs.next()) {
        int orderId = rs.getInt(1);
        order.setId(orderId);

        // Insert order-product-quantity and ingredient mappings (same as before)
        String insertProductSQL =
            "INSERT INTO Order_ProductQuantity (order_id, product_id, quantity) VALUES (?, ?, ?)";
        String insertIngredientSQL =
            "INSERT INTO Order_ProductQuantity_Ingredient (order_product_quantity_id, ingredient_id, quantity) VALUES (?, ?, ?)";

        try (PreparedStatement productStmt =
                connection.prepareStatement(insertProductSQL, Statement.RETURN_GENERATED_KEYS);
            PreparedStatement ingredientStmt = connection.prepareStatement(insertIngredientSQL)) {
          for (ProductQuantity pq : order.getProductQuantity()) {
            productStmt.setInt(1, orderId);
            productStmt.setInt(2, pq.getCustomizedProduct().getProduct().getId());
            productStmt.setInt(3, pq.getQuantity());
            productStmt.executeUpdate();

            ResultSet productKeys = productStmt.getGeneratedKeys();
            if (productKeys.next()) {
              int orderProductQuantityId = productKeys.getInt(1);

              Map<Ingredient, Integer> ingredients =
                  pq.getCustomizedProduct().getIngredientquanities();
              for (Map.Entry<Ingredient, Integer> entry : ingredients.entrySet()) {
                ingredientStmt.setInt(1, orderProductQuantityId);
                ingredientStmt.setInt(2, entry.getKey().getId());
                ingredientStmt.setInt(3, entry.getValue());
                ingredientStmt.addBatch();
              }
            }
          }
          ingredientStmt.executeBatch();
        }
        return order;
      }
    }
    throw new SQLException("Failed to save order, no ID obtained.");
  }

  public Optional<Order> findById(Integer id) throws SQLException {
    // Updated to fetch all necessary fields + product info for mapping
    String sql =
        "SELECT o.id AS order_id, o.feedback, o.coupon_code, c.discount, o.is_member, o.member_id, o.is_receipt, o.type, o.is_paid, o.created_at, "
            + "opq.product_id, opq.quantity, p.name, p.description, p.price, p.image_url, p.specialLabel, p.isASide, "
            +
            // assuming tags and tag_ids fetched by some join or aggregate function; adapt as
            // necessary
            "GROUP_CONCAT(t.name) AS tags, GROUP_CONCAT(t.id) AS tag_ids "
            + "FROM Orders o "
            + "LEFT JOIN Coupons c ON o.coupon_code = c.code "
            + "LEFT JOIN Order_ProductQuantity opq ON o.id = opq.order_id "
            + "LEFT JOIN Product p ON opq.product_id = p.id "
            + "LEFT JOIN Product_Tags pt ON p.id = pt.product_id "
            + "LEFT JOIN Tags t ON pt.tag_id = t.id "
            + "WHERE o.id = ? "
            + "GROUP BY o.id, opq.id";

    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setInt(1, id);
      ResultSet rs = stmt.executeQuery();
      Order order = orderMapper.map(rs);
      return Optional.ofNullable(order);
    }
  }

  public List<Order> findAll() throws SQLException {
    List<Order> orders = new ArrayList<>();
    String sql = "SELECT id FROM Orders";

    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        int id = rs.getInt("id");
        findById(id).ifPresent(orders::add);
      }
    }

    return orders;
  }

  public void update(Order order) throws SQLException {
    // Update Orders table with all fields
    String updateOrderSql =
        "UPDATE Orders SET feedback = ?, coupon_code = ?, is_member = ?, member_id = ?, is_receipt = ?, type = ?, is_paid = ?, created_at = ? WHERE id = ?";

    try (PreparedStatement updateStmt = connection.prepareStatement(updateOrderSql)) {
      updateStmt.setInt(1, order.getFeedback());

      if (order.getDiscount() != null && order.getDiscount().isPresent()) {
        updateStmt.setString(2, order.getDiscount().get().getCode());
      } else {
        updateStmt.setNull(2, Types.VARCHAR);
      }

      updateStmt.setBoolean(3, order.getMemberId().isPresent());

      if (order.getMemberId().isPresent()) {
        updateStmt.setInt(4, order.getMemberId().get());
      } else {
        updateStmt.setNull(4, Types.INTEGER);
      }

      updateStmt.setBoolean(5, order.isReceipt());
      updateStmt.setString(6, order.getType().name());
      updateStmt.setBoolean(7, order.isPaid());
      updateStmt.setTimestamp(8, Timestamp.valueOf(order.getCreatedAt()));
      updateStmt.setInt(9, order.getId());

      updateStmt.executeUpdate();
    }

    // Delete and reinsert product quantities and ingredients as before
    String deleteSql = "DELETE FROM Order_ProductQuantity WHERE order_id = ?";
    try (PreparedStatement deleteStmt = connection.prepareStatement(deleteSql)) {
      deleteStmt.setInt(1, order.getId());
      deleteStmt.executeUpdate();
    }

    String insertProductSql =
        "INSERT INTO Order_ProductQuantity (order_id, product_id, quantity) VALUES (?, ?, ?)";

    String insertIngredientSql =
        "INSERT INTO Order_ProductQuantity_Ingredient (order_product_quantity_id, ingredient_id, quantity) VALUES (?, ?, ?)";

    try (PreparedStatement productStmt =
            connection.prepareStatement(insertProductSql, Statement.RETURN_GENERATED_KEYS);
        PreparedStatement ingredientStmt = connection.prepareStatement(insertIngredientSql)) {
      for (ProductQuantity pq : order.getProductQuantity()) {
        productStmt.setInt(1, order.getId());
        productStmt.setInt(2, pq.getCustomizedProduct().getProduct().getId());
        productStmt.setInt(3, pq.getQuantity());
        productStmt.executeUpdate();

        ResultSet productKeys = productStmt.getGeneratedKeys();
        if (productKeys.next()) {
          int orderProductQuantityId = productKeys.getInt(1);

          Map<Ingredient, Integer> ingredients = pq.getCustomizedProduct().getIngredientquanities();
          for (Map.Entry<Ingredient, Integer> entry : ingredients.entrySet()) {
            ingredientStmt.setInt(1, orderProductQuantityId);
            ingredientStmt.setInt(2, entry.getKey().getId());
            ingredientStmt.setInt(3, entry.getValue());
            ingredientStmt.addBatch();
          }
        }
      }
      ingredientStmt.executeBatch();
    }
  }

  public void delete(Integer id) throws SQLException {
    try (PreparedStatement stmt =
        connection.prepareStatement("DELETE FROM Order_ProductQuantity WHERE order_id = ?")) {
      stmt.setInt(1, id);
      stmt.executeUpdate();
    }

    try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM Orders WHERE id = ?")) {
      stmt.setInt(1, id);
      stmt.executeUpdate();
    }
  }
}
