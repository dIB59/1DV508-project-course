package org.example.features.ingredients;

import java.util.Objects;
import org.example.database.Identifiable;

public class Ingredient implements Identifiable<Integer> {
  private int id;
  private String name;
  private double price;

  public Ingredient(int id, String name, double price) {
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("Name cannot be null or empty");
    }

    if (price < 0) {
      throw new IllegalArgumentException("Price cannot be negative");
    }

    this.id = id;
    this.name = name;
    this.price = price;
  }

  public Ingredient(String name, double price) {
    this(0, name, price);
  }

  public Integer getId() {
    return id;
  }

  public void setID(int id) {
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

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    if (price < 0) {
      throw new IllegalArgumentException("Price cannot be negative");
    }
    this.price = price;
  }

  @Override
  public String toString() {
    return "Ingredient{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", price=" + price +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Ingredient that = (Ingredient) o;
    return id == that.id;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
