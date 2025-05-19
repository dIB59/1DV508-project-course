package org.example.features.ingredients;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.example.database.CrudRepository;
import org.example.database.EntityMapper;
import org.example.features.product.Product;

public class IngredientsRepository {
    private final Connection connection;
    private final String tableName = "Ingredients";
    private final EntityMapper<Ingredient> mapper;

    public IngredientsRepository(Connection connection, EntityMapper<Ingredient> mapper) {
        this.connection = connection;
        this.mapper = mapper;
    }

    public Optional<Ingredient> getIngredientsForProduct(Product product) throws SQLException {
        int productId = product.getId();

        String sql = """
                SELECT i.* FROM Ingridients i
                JOIN Product_ingredients pi on i.id = pi.ingredients_id
                WHERE pi.product_id = ?
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapper.map(rs));

            }
        }
        return Optional.empty();
    }

    public Ingredient save(Ingredient entity) throws SQLException {
        String sql = """
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
}
