package org.example.features.product;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.example.database.Identifiable;

/**
 * Represents a product in the system.
 *
 * <p>This class encapsulates the properties of a product, including its ID, name,
 * description, price, image URL, and associated tags. It provides validation for the name,
 * description, and price fields to ensure they are not null or empty and that the price is not negative.
 */

public class Product implements Identifiable<Integer> {
  private int id;
  private String name;
  private String description;
  private double price;
  private String imageUrl;
  private String specialLabel;
  private Boolean isASide;
  private List<Tag> tags;
  private String ingredients;


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
  public Product(int id, String name, String description, double price, String imageUrl, String specialLabel,boolean isASide, List<Tag> tags) {
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
  }

  public Product(String name, String description, double price, String imageUrl, String specialLabel, boolean isASide) {
    this(0, name, description, price, imageUrl, specialLabel, isASide,List.of());
  }

  public Product(String name, String description, double price, String imageUrl, String specialLabel, boolean isASide,List<Tag> tags) {
    this(0, name, description, price, imageUrl, specialLabel, isASide,tags);
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

  public String getIngredients(){return ingredients; }

  public void setIngredients(String ingredients) {
    this.ingredients = ingredients;
  }

  public List<String> getIngredientsList() {
    return Arrays.asList(this.ingredients.split(","));
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

  /**
   * Gets tags.
   *
   * @return the tags
   */

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
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

  @Override
  public String toString() {
    return String.format(
        "Product{id=%d, name='%s', description='%s', price=%.2f, imageUrl='%s', tags=%s, ingredients='%s'}",
        id, name, description, price, imageUrl, tags, ingredients
    );
  }
}
