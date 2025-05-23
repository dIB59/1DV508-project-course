package org.example.features.product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.example.database.EntityMapper;
import org.example.features.ingredients.*;

/** The type Product mapper. */
public class ProductMapper implements EntityMapper<Product> {

  private final IngredientsRepository ingredientRepository;

  public ProductMapper(IngredientsRepository ingredientRepository) {
    this.ingredientRepository = ingredientRepository;
  }

  @Override
  public Product map(ResultSet rs) throws SQLException {
    // Parse tags
    String tagsString = rs.getString("tags");
    List<String> tagsName = new ArrayList<>();
    if (tagsString != null && !tagsString.isEmpty()) {
      tagsName.addAll(Arrays.asList(tagsString.split(",")));
    }

    String tagIdsString = rs.getString("tags_ids");
    List<Integer> tagIds = new ArrayList<>();
    if (tagIdsString != null && !tagIdsString.isEmpty()) {
      for (String tagId : tagIdsString.split(",")) {
        try {
          tagIds.add(Integer.parseInt(tagId));
        } catch (NumberFormatException e) {
          throw new RuntimeException("Failed to parse tag ID: " + tagId, e);
        }
      }
    }

    List<Tag> tags = new ArrayList<>();
    for (int i = 0; i < Math.min(tagsName.size(), tagIds.size()); i++) {
      tags.add(new Tag(tagIds.get(i), tagsName.get(i)));
    }

    int id = rs.getInt("id");

    Product product =
        new Product(
            id,
            rs.getString("name"),
            rs.getString("description"),
            rs.getDouble("price"),
            rs.getString("image_url"),
            rs.getString("specialLabel"),
            rs.getBoolean("isASide"),
            tags);

    // Set the imageBytes from the LONGBLOB column
    byte[] imageBytes = rs.getBytes("image");
    product.setImageBytes(imageBytes);

    // Parse ingredients
    String ingredientsNamesString = rs.getString("ingredients_name");
    String ingredientsIdsString = rs.getString("ingredients_ids");
    String ingredientsPricesString = rs.getString("ingredients_price");

    if (ingredientsNamesString != null
        && ingredientsIdsString != null
        && !ingredientsNamesString.isEmpty()
        && !ingredientsIdsString.isEmpty()) {
      String[] ingredientNames = ingredientsNamesString.split(",");
      String[] ingredientIds = ingredientsIdsString.split(",");
      String[] ingredientPrices = ingredientsPricesString.split(",");

      for (int i = 0; i < Math.min(ingredientNames.length, ingredientIds.length); i++) {
        try {
          int ingredientId = Integer.parseInt(ingredientIds[i]);
          String ingredientName = ingredientNames[i];
          double ingredientPrice = Double.parseDouble(ingredientPrices[i]);
          Ingredient ingredient = new Ingredient(ingredientId, ingredientName, ingredientPrice);
          product.addIngredient(ingredient, 1);
        } catch (NumberFormatException e) {
          throw new RuntimeException("Failed to parse ingredient ID: " + ingredientIds[i], e);
        }
      }
    }
    return product;
  }
}
