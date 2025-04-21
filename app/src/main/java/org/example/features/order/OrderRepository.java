package org.example.features.order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.example.features.product.Product;
import org.example.shared.CrudRepository;
import org.example.shared.EntityMapper;


/**
 * The type Order repository.
 */
public class OrderRepository implements CrudRepository<Order> {

  private final Connection connection;
  private final EntityMapper<Order> orderMapper;

  /**
   * Instantiates a new Order repository.
   *
   * @param connection  the connection
   * @param orderMapper the order mapper
   */
  public OrderRepository(Connection connection, EntityMapper<Order> orderMapper) {
    this.connection = connection;
    this.orderMapper = orderMapper;
  }

  public void save(Order order) throws SQLException {
    String insertOrderSQL = "INSERT INTO Orders () VALUES ()";
    try (PreparedStatement orderStmt = connection.prepareStatement(insertOrderSQL,
        Statement.RETURN_GENERATED_KEYS)) {
      orderStmt.executeUpdate();
      ResultSet rs = orderStmt.getGeneratedKeys();
      if (rs.next()) {
        int orderId = rs.getInt(1);
        order.setId(orderId);

        String insertProductSQL =
            "INSERT INTO Order_ProductQuantity (order_id, product_id, quantity) VALUES (?, ?, ?)";
        try (PreparedStatement productStmt = connection.prepareStatement(insertProductSQL)) {
          for (ProductQuantity pq : order.getProductQuantity()) {
            productStmt.setInt(1, orderId);
            productStmt.setInt(2, pq.getProduct().getId());
            productStmt.setInt(3, pq.getQuantity());
            productStmt.addBatch();
          }
          productStmt.executeBatch();
        }
      }
    }
  }

  public Optional<Order> findById(int id) throws SQLException {
    String sql =
        "SELECT opq.product_id, opq.quantity, p.name, p.description, p.price, p.image_url " +
            "FROM Order_ProductQuantity opq " +
            "JOIN Product p ON opq.product_id = p.id " +
            "WHERE opq.order_id = ?";

    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setInt(1, id);
      ResultSet rs = stmt.executeQuery();

      List<ProductQuantity> productQuantities = new ArrayList<>();
      while (rs.next()) {
        Product product = new Product(
            rs.getInt("product_id"),
            rs.getString("name"),
            rs.getString("description"),
            rs.getDouble("price"),
            rs.getString("imageUrl")
        );
        int quantity = rs.getInt("quantity");
        productQuantities.add(new ProductQuantity(product, quantity));
      }

      if (productQuantities.isEmpty()) {
        return Optional.empty();
      }
      Order order = new Order(id, productQuantities);
      return Optional.of(order);
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
    // Delete existing order-product mappings
    String deleteSQL = "DELETE FROM Order_ProductQuantity WHERE order_id = ?";
    try (PreparedStatement deleteStmt = connection.prepareStatement(deleteSQL)) {
      deleteStmt.setInt(1, order.getId());
      deleteStmt.executeUpdate();
    }

    // Re-insert updated product quantities
    String insertSQL =
        "INSERT INTO Order_ProductQuantity (order_id, product_id, quantity) VALUES (?, ?, ?)";
    try (PreparedStatement stmt = connection.prepareStatement(insertSQL)) {
      for (ProductQuantity pq : order.getProductQuantity()) {
        stmt.setInt(1, order.getId());
        stmt.setInt(2, pq.getProduct().getId());
        stmt.setInt(3, pq.getQuantity());
        stmt.addBatch();
      }
      stmt.executeBatch();
    }
  }

  public void delete(int id) throws SQLException {
    try (PreparedStatement stmt = connection.prepareStatement(
        "DELETE FROM Order_ProductQuantity WHERE order_id = ?")) {
      stmt.setInt(1, id);
      stmt.executeUpdate();
    }

    try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM Orders WHERE id = ?")) {
      stmt.setInt(1, id);
      stmt.executeUpdate();
    }
  }
}

