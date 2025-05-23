package org.example.features.product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
import org.example.database.CrudRepository;
import org.example.database.EntityMapper;
import org.example.features.ingredients.Ingredient;

/** The type Product repository. */
public class ProductRepository implements CrudRepository<Product, Integer> {

  private static final Logger log = Logger.getLogger(ProductRepository.class.getName());
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

  public Optional<Product> findById(Integer id) throws SQLException {
    String sql =
        "SELECT Product.id, Product.name, Product.description, Product.price, Product.image_url, GROUP_CONCAT(T.name) AS tags, GROUP_CONCAT(T.id) AS tags_ids "
            + "FROM Product "
            + "LEFT JOIN Product_Tags PT ON Product.id = PT.product_id "
            + "LEFT JOIN Tags T ON PT.tag_id = T.id "
            + "WHERE Product.id = ? "
            + "GROUP BY Product.id";

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
    String sql =
        "SELECT Product.id, Product.name, Product.description, Product.price,"
            + "Product.image_url, Product.specialLabel, Product.isASide, Product.image, "
            + "GROUP_CONCAT(T.name) AS tags, GROUP_CONCAT(T.id) AS tags_ids, "
            + "GROUP_CONCAT(Ing.name) AS ingredients_name, GROUP_CONCAT(Ing.id) AS ingredients_ids, "
            + "GROUP_CONCAT(Ing.price) AS ingredients_price "
            + "FROM Product "
            + "LEFT JOIN Product_Tags PT ON Product.id = PT.product_id "
            + "LEFT JOIN Tags T ON PT.tag_id = T.id "
            + "LEFT JOIN Product_ingredients PI ON Product.id = PI.product_id "
            + "LEFT JOIN Ingredients Ing ON PI.ingredients_id = Ing.id "
            + "GROUP BY Product.id";
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
            + " (name, price, description, image_url, specialLabel, isASide, image) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?)";

    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setString(1, entity.getName());
      stmt.setDouble(2, entity.getPrice());
      stmt.setString(3, entity.getDescription());
      stmt.setString(4, entity.getImageUrl());
      stmt.setString(5, entity.getSpecialLabel());
      stmt.setBoolean(6, entity.getisASide());
      stmt.setBytes(7, entity.getImageBytes());
      stmt.executeUpdate();
    }

    log.info("Product saved: " + entity.getName());

    String sql2 = "SELECT LAST_INSERT_ID()";
    try (PreparedStatement stmt = connection.prepareStatement(sql2);
        ResultSet rs = stmt.executeQuery()) {
      if (rs.next()) {
        int id = rs.getInt(1);

        // Save tags
        if (entity.getTags() != null && !entity.getTags().isEmpty()) {
          addTagsToProduct(id, entity.getTags());
        }

        // Save ingredients
        if (entity.getIngredients() != null && !entity.getIngredients().isEmpty()) {
          addIngredientsToProduct(id, entity.getIngredients().keySet().stream().toList());
        }

        return new Product(
            id,
            entity.getName(),
            entity.getDescription(),
            entity.getPrice(),
            entity.getImageUrl(),
            entity.getSpecialLabel(),
            entity.getisASide(),
            entity.getTags(),
            entity.getIngredients().keySet().stream().toList());
      }
    }

    throw new SQLException("Failed to save product, no ID obtained.");
  }

  public void update(Product entity) throws SQLException {
    String sql =
        "UPDATE "
            + tableName
            + " SET name = ?, price = ?, description = ?, image_url = ?, isASide = ?, specialLabel = ?, image = ? WHERE id = ?";

    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setString(1, entity.getName());
      stmt.setDouble(2, entity.getPrice());
      stmt.setString(3, entity.getDescription());
      stmt.setString(4, entity.getImageUrl());
      stmt.setBoolean(5, entity.getisASide());
      stmt.setString(6, entity.getSpecialLabel());
      stmt.setBytes(7, entity.getImageBytes());
      stmt.setInt(8, entity.getId());
      stmt.executeUpdate();
    }

    // Delete old tags
    try (PreparedStatement stmt =
        connection.prepareStatement("DELETE FROM Product_Tags WHERE product_id = ?")) {
      stmt.setInt(1, entity.getId());
      stmt.executeUpdate();
    }

    // Re-insert tags
    Map<String, Integer> tagNameToId = new HashMap<>();
    for (Tag tag : findAllTags()) {
      tagNameToId.put(tag.getName(), tag.getId());
    }

    if (entity.getTags() != null && !entity.getTags().isEmpty()) {
      try (PreparedStatement stmt =
          connection.prepareStatement(
              "INSERT INTO Product_Tags (product_id, tag_id) VALUES (?, ?)")) {
        for (Tag tag : entity.getTags()) {
          Integer tagId = tagNameToId.get(tag.getName());
          if (tagId == null) {
            throw new SQLException("Tag name not found: " + tag.getName());
          }
          stmt.setInt(1, entity.getId());
          stmt.setInt(2, tagId);
          stmt.addBatch();
        }
        stmt.executeBatch();
      }
    }

    // ✅ Delete old ingredients
    try (PreparedStatement stmt =
        connection.prepareStatement("DELETE FROM Product_ingredients WHERE product_id = ?")) {
      stmt.setInt(1, entity.getId());
      stmt.executeUpdate();
    }

    // ✅ Re-insert ingredients
    if (entity.getIngredients() != null && !entity.getIngredients().isEmpty()) {
      addIngredientsToProduct(entity.getId(), entity.getIngredients().keySet().stream().toList());
    }
  }

  private void addTagsToProduct(int productId, List<Tag> tags) throws SQLException {
    Map<String, Integer> tagNameToId = new HashMap<>();
    for (Tag tag : findAllTags()) {
      tagNameToId.put(tag.getName(), tag.getId());
    }

    String insertTagSql = "INSERT INTO Product_Tags (product_id, tag_id) VALUES (?, ?)";
    try (PreparedStatement stmt = connection.prepareStatement(insertTagSql)) {
      for (Tag tag : tags) {
        Integer tagId = tagNameToId.get(tag.getName());
        if (tagId == null) {
          throw new SQLException("Tag name not found: " + tag.getName());
        }
        stmt.setInt(1, productId);
        stmt.setInt(2, tagId);
        stmt.addBatch();
      }
      stmt.executeBatch();
    }
  }

  public void delete(Integer id) throws SQLException {
    String sql = "DELETE FROM " + tableName + " WHERE id = ?";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setInt(1, id);
      stmt.executeUpdate();
    }
  }

  public List<Tag> findAllTags() {
    List<Tag> tags = new ArrayList<>();
    String sql = "SELECT id, name FROM Tags";
    try {
      try (PreparedStatement stmt = connection.prepareStatement(sql);
          ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          tags.add(new Tag(rs.getInt("id"), rs.getString("name")));
        }
      }
      return tags;
    } catch (SQLException e) {
      log.severe("Error fetching tags: " + e.getMessage());
      return new ArrayList<>();
    }
  }

  public void addTagToProduct(int productId, int tagId) throws SQLException {
    String sql = "INSERT INTO Product_Tags (product_id, tag_id) VALUES (?, ?)";

    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setInt(1, productId);
      stmt.setInt(2, tagId);
      stmt.executeUpdate();
    }
  }

  public void removeTagFromProduct(int productId, int tagId) throws SQLException {
    String sql = "DELETE FROM Product_Tags WHERE product_id = ? AND tag_id = ?";

    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setInt(1, productId);
      stmt.setInt(2, tagId);
      stmt.executeUpdate();
    }
  }

  /**
   * @param tagName name if the tag you want to create
   * @return the id of the tag created
   * @throws SQLException if tag is not created properly
   */
  public int createTag(String tagName) throws SQLException {
    String sql = "INSERT INTO Tags (name) VALUES (?)";
    PreparedStatement stmt = connection.prepareStatement(sql);
    stmt.setString(1, tagName);
    stmt.executeUpdate();
    String sql2 = "SELECT LAST_INSERT_ID()";
    PreparedStatement stmt2 = connection.prepareStatement(sql2);
    ResultSet rs = stmt2.executeQuery();
    if (rs.next()) {
      return rs.getInt(1);
    }
    throw new SQLException("Failed to create tag, no ID obtained.");
  }

  public void addIngredientsToProduct(int productId, List<Ingredient> ingredients) {
    String sql = "INSERT INTO Product_ingredients (product_id, ingredients_id) VALUES (?, ?)";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
      for (Ingredient ingredient : ingredients) {
        stmt.setInt(1, productId);
        stmt.setInt(2, ingredient.getId());
        stmt.addBatch();
      }
      stmt.executeBatch();
    } catch (SQLException e) {
      log.severe("Error adding ingredients to product: " + e.getMessage());
    }
  }
}
