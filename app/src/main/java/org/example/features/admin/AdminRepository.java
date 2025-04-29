package org.example.features.admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.example.database.CrudRepository;

public class AdminRepository implements CrudRepository<Admin, Integer> {

  private final AdminMapper adminMapper;
  private final Connection connection;

  public AdminRepository(Connection connection, AdminMapper adminMapper) {
    this.connection = connection;
    this.adminMapper = adminMapper;
  }

  @Override
  public Admin save(Admin entity) throws SQLException {
    String sql = "INSERT INTO Admin (username, password) VALUES (?, ?)";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setString(1, entity.getUsername());
      stmt.setString(2, entity.getPassword());
      stmt.executeUpdate();
    }
    String sql2 = "SELECT LAST_INSERT_ID()";
    try (PreparedStatement stmt = connection.prepareStatement(sql2);
        ResultSet rs = stmt.executeQuery()) {
      if (rs.next()) {
        int id = rs.getInt(1);
        return new Admin(id, entity.getUsername(), entity.getPassword());
      }
    }
    throw new SQLException("Failed to retrieve the last inserted ID.");
  }

  @Override
  public Optional<Admin> findById(Integer id) throws SQLException {
    String sql = "SELECT * FROM Admin WHERE id = ?";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setInt(1, id);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        return Optional.of(adminMapper.map(rs));
      }
    }
    return Optional.empty();
  }

  @Override
  public void update(Admin entity) throws SQLException {
    String sql = "UPDATE Admin SET username = ?, password = ? WHERE id = ?";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setString(1, entity.getUsername());
      stmt.setString(2, entity.getPassword());
      stmt.executeUpdate();
    }
  }

  @Override
  public void delete(Integer id) throws SQLException {
    String sql = "DELETE FROM Admin WHERE id = ?";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setInt(1, id);
      stmt.executeUpdate();
    }
  }

  @Override
  public List<Admin> findAll() throws SQLException {
    String sql = "SELECT * FROM Admin";
    try (PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery()) {
      List<Admin> admins = new ArrayList<>();
      while (rs.next()) {
        admins.add(adminMapper.map(rs));
      }
      return admins;
    }
  }

  public Optional<Admin> findByUsername(String username) {
    String sql = "SELECT * FROM Admin WHERE username = ?";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setString(1, username);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        return Optional.of(adminMapper.map(rs));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return Optional.empty();
  }
}
