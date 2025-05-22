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
          throw new RuntimeException("Shit went wrong");
        }
      }
    }

    List<Tag> tags = new ArrayList<>();
    for (int i = 0; i < tagsName.size(); i++) {
      tags.add(new Tag(tagIds.get(i), tagsName.get(i)));
    }

    int id = rs.getInt("id");

    Product product = new Product(
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

    List<Ingredient> ingredients = ingredientRepository.getIngredientsForProduct(product);
      for (Ingredient ingredient : ingredients) {
        product.addIngredient(ingredient, 1);
      }

    return product;
  }
}
