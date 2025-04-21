package org.example.features.product;

public record Product(
    int id,
    String name,
    String description,
    double price,
    String imageUrl
) {
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

  public String getName() {
    return name;
  }

  public double getPrice() {
    return price;
  }

  public int getId() {
    return id;
  }

  public String getDescription() {
    return description;
  }

  public String getImageUrl() {
    return imageUrl;
  }
}
