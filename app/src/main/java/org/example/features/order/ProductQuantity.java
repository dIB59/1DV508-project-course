package org.example.features.order;

import org.example.features.product.Product;

public class ProductQuantity {

  private final Product product;
  private int quantity;

  public ProductQuantity(Product product, int quantity) {
    this.product = product;
    this.quantity = quantity;
  }

  public void addQuantity(int quantity) {
    this.quantity += quantity;
  }

  public void subtractQuantity(int quantity) {
    this.quantity -= quantity;
  }

  public int getProductId() {
    return product.getId();
  }

  public double getPrice() {
    return product.getPrice() * quantity;
  }

  public int getQuantity() {
    return quantity;
  }

  public Product getProduct() {
    return product;
  }
}
