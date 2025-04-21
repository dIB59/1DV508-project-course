package org.example.features.product;

import java.sql.*;
import java.util.*;
import org.example.shared.CrudRepository;
import org.example.shared.EntityMapper;

public class ProductRepository implements CrudRepository<Product> {

  private final Connection connection;
  private final String tableName;
  private final EntityMapper<Product> mapper;

  public ProductRepository(Connection connection, String tableName, EntityMapper<Product> mapper) {
    this.connection = connection;
    this.tableName = tableName;
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

  public void save(Product entity) throws SQLException {
    String sql =
        "INSERT INTO " + tableName + " (name, price, description, image_url) " +
        "VALUES (?, ?, ?, ?)";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setString(1, entity.getName());
      stmt.setDouble(2, entity.getPrice());
      stmt.setString(3, entity.getDescription());
      stmt.setString(4, entity.getImageUrl());
      stmt.executeUpdate();
    }
  }

  public void update(Product entity) throws SQLException {
    String sql =
        "UPDATE " + tableName + " SET name = ?, price = ?, description = ?, image_url = ? " +
        "WHERE id = ?";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setString(1, entity.getName());
      stmt.setDouble(2, entity.getPrice());
      stmt.setString(3, entity.getDescription());
      stmt.setString(4, entity.getImageUrl());
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
