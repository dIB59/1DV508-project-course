package org.example.features.product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.example.database.CrudRepository;
import org.example.database.EntityMapper;

/** The type Product repository. */
public class ProductRepository implements CrudRepository<Product> {

  private final Connection connection;
  private final String tableName = "Product";
  private final EntityMapper<Product> mapper;

  /**
   * Instantiates a new Product repository.
   *
   * @param connection the connection
   * @param mapper the mapper
   */
  public ProductRepository(Connection connection, EntityMapper<Product> mapper) {
    this.connection = connection;
    this.mapper = mapper;
  }

  public Optional<Product> findById(int id) throws SQLException {
    String sql = "SELECT * FROM " + tableName + " WHERE id = ?";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setInt(1, id);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        return Optional.of(mapper.map(rs));
      }
    }
    return Optional.empty();
  }

  public List<Product> findAll() throws SQLException {
    String sql = "SELECT * FROM " + tableName;
    List<Product> results = new ArrayList<>();
    try (PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery()) {
      while (rs.next()) {
        results.add(mapper.map(rs));
      }
    }
    return results;
  }

  public Product save(Product entity) throws SQLException {
    String sql =
        "INSERT INTO "
            + tableName
            + " (name, price, description, image_url) "
            + "VALUES (?, ?, ?, ?)";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setString(1, entity.getName());
      stmt.setDouble(2, entity.getPrice());
      stmt.setString(3, entity.getDescription());
      stmt.setString(4, entity.getImageUrl());
      stmt.executeUpdate();
    }
    String sql2 = "SELECT LAST_INSERT_ID()";
    try (PreparedStatement stmt = connection.prepareStatement(sql2);
        ResultSet rs = stmt.executeQuery()) {
      if (rs.next()) {
        int id = rs.getInt(1);
        return new Product(
            id, entity.getName(), entity.getDescription(), entity.getPrice(), entity.getImageUrl());
      }
    }
    throw new SQLException("Failed to save product, no ID obtained.");
  }

  public void update(Product entity) throws SQLException {
    String sql =
        "UPDATE "
            + tableName
            + " SET name = ?, price = ?, description = ?, image_url = ? "
            + "WHERE id = ?";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setString(1, entity.getName());
      stmt.setDouble(2, entity.getPrice());
      stmt.setString(3, entity.getDescription());
      stmt.setString(4, entity.getImageUrl());
      stmt.setInt(5, entity.getId());
      stmt.executeUpdate();
    }
  }

  public void delete(int id) throws SQLException {
    String sql = "DELETE FROM " + tableName + " WHERE id = ?";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setInt(1, id);
      stmt.executeUpdate();
    }
  }
}
