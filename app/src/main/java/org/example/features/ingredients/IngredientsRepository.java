package org.example.features.ingredients;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.example.database.EntityMapper;
import org.example.features.product.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IngredientsRepository {
  private static final Logger log = LoggerFactory.getLogger(IngredientsRepository.class);
  private final Connection connection;
  private final String tableName = "Ingredients";
  private final EntityMapper<Ingredient> mapper;

  public IngredientsRepository(Connection connection, EntityMapper<Ingredient> mapper) {
    this.connection = connection;
    this.mapper = mapper;
  }

  public List<Ingredient> getIngredientsForProduct(Product product) throws SQLException {
    int productId = product.getId();

    String sql =
        """
                SELECT i.* FROM Ingredients i
                JOIN Product_ingredients pi on i.id = pi.ingredients_id
                WHERE pi.product_id = ?
                """;

    List<Ingredient> ingredients = new ArrayList<>();

    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setInt(1, productId);
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        ingredients.add(mapper.map(rs));
      }
    }
    return ingredients;
  }

  public Optional<Ingredient> findById(Integer id) throws SQLException {
    String sql = "SELECT * FROM Ingredients WHERE id = ?";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setInt(1, id);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        return Optional.of(mapper.map(rs));
      }
    }
    return Optional.empty();
  }

  public Ingredient save(Ingredient entity) throws SQLException {
    String sql =
        """
                INSERT INTO Ingredients (name, price)
                VALUES (?, ?)
                """;

    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setString(1, entity.getName());
      stmt.setDouble(2, entity.getPrice());

      stmt.executeUpdate();
    }
    String sql2 = "SELECT LAST_INSERT_ID()";
    try (PreparedStatement stmt = connection.prepareStatement(sql2);
        ResultSet rs = stmt.executeQuery()) {
      if (rs.next()) {
        int id = rs.getInt(1);
        return new Ingredient(id, entity.getName(), entity.getPrice());
      }
    }
    throw new SQLException("Failed to save product, no ID obtained.");
  }

  public void delete(Integer id) throws SQLException {

    String sql = "DELETE FROM " + tableName + " WHERE id = ?";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setInt(1, id);
      stmt.executeUpdate();
    }
  }

  public void update(Ingredient ingredient) throws SQLException {
    String sql = "UPDATE Ingredients SET name = ?, price = ? WHERE id = ?";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setString(1, ingredient.getName());
      stmt.setDouble(2, ingredient.getPrice());
      stmt.setInt(3, ingredient.getId());
      stmt.executeUpdate();
    }
  }

  public List<Ingredient> findAll() {
    String sql = "SELECT * FROM " + tableName;
    List<Ingredient> ingredients = new ArrayList<>();
    try (PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery()) {
      while (rs.next()) {
        ingredients.add(mapper.map(rs));
      }
    } catch (SQLException e) {
      log.error("Error fetching all ingredients", e);
    }
    return ingredients;
  }
}
