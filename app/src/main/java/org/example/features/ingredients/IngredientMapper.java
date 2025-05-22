package org.example.features.ingredients;

import java.sql.SQLException;
import java.sql.ResultSet;
import org.example.features.product.Product;
import org.example.database.EntityMapper;

public class IngredientMapper implements EntityMapper<Ingredient> {
  @Override
  public Ingredient map(ResultSet rs) throws SQLException {
    return new Ingredient(rs.getInt("id"), rs.getString("name"), rs.getDouble("price"));
  }
}
