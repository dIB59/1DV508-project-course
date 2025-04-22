package org.example.features.product;

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
public record Product(int id, String name, String description, double price, String imageUrl) {
  /**
   * Instantiates a new Product.
   *
   * @param id the id
   * @param name the name
   * @param description the description
   * @param price the price
   * @param imageUrl the image url
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

  @Override
  public String toString() {
    return String.format("%s, Price: %.2f", name, price);
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
}
