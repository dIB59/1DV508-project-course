package org.example.features.product;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.image.Image;
import org.example.database.Identifiable;
import org.example.features.ingredients.Ingredient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a product in the system.
 *
 * <p>This class encapsulates the properties of a product, including its ID, name,
 * description, price, image URL, and associated tags. It provides validation for the name,
 * description, and price fields to ensure they are not null or empty and that the price is not negative.
 */

public class Product implements Identifiable<Integer> {
  private static final Logger log = LoggerFactory.getLogger(Product.class);
  private int id;
  private String name;
  private String description;
  private double price;
  private String imageUrl;
  private byte[] imageBytes;
  private final String specialLabel;
  private final Boolean isASide;
  private List<Tag> tags;
  private Map<Ingredient, Integer> defaultIngredients = new HashMap<>();

  /**
   * Instantiates a new Product.
   *
   * @param id the id
   * @param name the name
   * @param description the description
   * @param price the price
   * @param imageUrl the image url
   * @param specialLabel The special label for the product (e.g., "Hot", "Deal").
   * @param tags the tags
   */
  public Product(int id, String name, String description, double price, String imageUrl, String specialLabel,boolean isASide, List<Tag> tags, List<Ingredient> ingredients) {
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("Name cannot be null or empty");
    }
    if (description == null || description.isBlank()) {
      throw new IllegalArgumentException("Description cannot be null or empty");
    }
    if (price < 0) {
      throw new IllegalArgumentException("Price cannot be negative");
    }
    this.id = id;
    this.name = name;
    this.description = description;
    this.price = price;
    this.imageUrl = imageUrl;
    this.tags = tags != null ? tags : new ArrayList<>();
    this.specialLabel = specialLabel;
    this.isASide = isASide;
    this.imageBytes = readImageBytesFromFile(imageUrl);
    for (Ingredient ingredient : ingredients) {
      this.defaultIngredients.put(ingredient, 1);
    }
  }
  public Product(String name, String description, double price, String imageUrl, String specialLabel, boolean isASide) {
    this(0, name, description, price, imageUrl, specialLabel, isASide, List.of(), new ArrayList<>());
  }

  public Product(String name, String description, double price, String imageUrl, String specialLabel, boolean isASide,List<Tag> tags) {
    this(0, name, description, price, imageUrl, specialLabel, isASide, tags, new ArrayList<>());
  }

  public Product(String name, String description, double price, String imageUrl, String specialLabel, boolean isASide, List<Tag> tags, List<Ingredient> ingredients) {
    this(0, name, description, price, imageUrl, specialLabel, isASide, tags, ingredients);
  }

  public Product(int id, String name, String description, double price, String imageUrl, String specialLabel, Boolean aBoolean, List<Tag> tags) {
    this(id, name, description, price, imageUrl, specialLabel, aBoolean, tags, new ArrayList<>());
  }

  public byte[] getImageBytes() {
    return imageBytes;
  }

  public void setImageBytes(byte[] imageBytes) {
    this.imageBytes = imageBytes;
  }

  public void loadImageFromFile(File file) {
    try {
      this.imageBytes = Files.readAllBytes(file.toPath());
      this.imageUrl = file.toURI().toString(); // keeps consistency with existing logic
    } catch (IOException e) {
      System.err.println("Failed to load image from file: " + file.getAbsolutePath());
      e.printStackTrace();
    }
  }

  public Image getImage() {
    if (imageBytes != null && imageBytes.length > 0) {
      return new Image(new ByteArrayInputStream(imageBytes));
    }
    return null; // or you can return a default image
  }

  public Integer getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;

  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("Name cannot be null or empty");
    }
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    if (description == null || description.isBlank()) {
      throw new IllegalArgumentException("Description cannot be null or empty");
    }
    this.description = description;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    if (price < 0) {
      throw new IllegalArgumentException("Price cannot be negative");
    }
    this.price = price;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  /**
   * Gets tags.
   *
   * @return the tags
   */

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

   /**
   * Gets the special label
   *
   * @return the special label
   */
  public String getSpecialLabel() {
    return specialLabel;
  }

  public Boolean getisASide(){
    return isASide;
  }

  public List<Tag> getTags() {
    return tags;
  }

  public void setTags(List<Tag> tags) {
    this.tags = tags != null ? tags : new ArrayList<>();
  }

  public List<Integer> tagIds() {
    List<Integer> tagIds = new ArrayList<>();
    for (Tag tag : tags) {
      tagIds.add(tag.getId());
    }
    return tagIds;
  }

  public Map<Ingredient, Integer> getIngredients() {
        return defaultIngredients;
  }

  public void setIngredients(Map<Ingredient, Integer> ingredients) {
    this.defaultIngredients = ingredients;
  }

  public void addIngredient(Ingredient ingredient, int quantity) {
    if (defaultIngredients.containsKey(ingredient)) {
        int currentQty = defaultIngredients.get(ingredient);
        defaultIngredients.put(ingredient, currentQty + quantity);
    } else {
        defaultIngredients.put(ingredient, quantity);
    }
}

  @Override
  public String toString() {
    return String.format(
        "Product{id=%d, name='%s', description='%s', price=%.2f, imageUrl='%s', tags=%s, ingredients='%s'}",
        id, name, description, price, imageUrl, tags, defaultIngredients
    );
  }

  private byte[] readImageBytesFromFile(String imageUrl) {
    if (imageUrl == null || imageUrl.isBlank()) return new byte[0];
    try {
      Path path = Paths.get(URI.create(imageUrl));
      return Files.readAllBytes(path);
    } catch (IOException | IllegalArgumentException e) {
      log.error("Error reading image file: {}", e.getMessage());
      log.error("Image URL: {}", imageUrl);
      return new byte[0];
    }
  }
}
