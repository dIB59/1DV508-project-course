package org.example.features.product;

import java.util.List;

/**
 * Represents a product in the system.
 *
 * <p>This class is a record that encapsulates the properties of a product, including its ID, name,
 * description, price, and image URL. It provides validation for the name, description, and price
 * fields to ensure they are not null or empty and that the price is not negative.
 *
 * @param id The unique identifier for the product.
 * @param name The name of the product.
 * @param description A brief description of the product.
 * @param price The price of the product.
 * @param imageUrl The URL of the product's image.
 */
public record Product(
    int id, String name, String description, double price, String imageUrl, String specialLabel, List<Tag> tags) {
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
  public Product {
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("Name cannot be null or empty");
    }
    if (description == null || description.isBlank()) {
      throw new IllegalArgumentException("Description cannot be null or empty");
    }
    if (price < 0) {
      throw new IllegalArgumentException("Price cannot be negative");
    }
  }

  public Product(String name, String description, double price, String imageUrl, String specialLabel) {
    this(0, name, description, price, imageUrl, specialLabel, List.of());
  }

  public Product(String name, String description, double price, String imageUrl, String specialLabel, List<Tag> tags) {
    this(0, name, description, price, imageUrl, specialLabel, tags);
  }

  @Override
  public String toString() {
    return String.format(
        "Product{id=%d, name='%s', description='%s', price=%.2f, imageUrl='%s', tags=%s}",
        id, name, description, price, imageUrl, specialLabel ,tags);
  }

  /**
   * Gets name.
   *
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Gets price.
   *
   * @return the price
   */
  public double getPrice() {
    return price;
  }

  /**
   * Gets id.
   *
   * @return the id
   */
  public int getId() {
    return id;
  }

  /**
   * Gets description.
   *
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * Gets image url.
   *
   * @return the image url
   */
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

  /**
   * Gets tags.
   *
   * @return the tags
   */
  public List<Tag> getTags() {
    return tags;
  }

  public List<Integer> tagIds() {
    List<Integer> tagIds = new java.util.ArrayList<>();
    for (Tag tag : tags) {
      tagIds.add(tag.getId());
    }
    return tagIds;
  }
}
