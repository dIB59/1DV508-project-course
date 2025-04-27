package org.example.features.product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.example.database.EntityMapper;

/** The type Product mapper. */
public class ProductMapper implements EntityMapper<Product> {

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

    return new Product(
        rs.getInt("id"),
        rs.getString("name"),
        rs.getString("description"),
        rs.getDouble("price"),
        rs.getString("image_url"),
        tags
        );
  }
}
