package org.example.features.order;

import java.util.ArrayList;
import java.util.List;
import org.example.features.product.Product;

public class Order {

  private int id;
  private final List<ProductQuantity> productQuantity;

  public Order(int id, List<ProductQuantity> productQuantity) {
    this.id = id;
    this.productQuantity = productQuantity;
  }

  public Order() {
    this.id = 0;
    this.productQuantity = new ArrayList<>();
  }

  public void addProduct(Product product) {
    for (ProductQuantity pq : productQuantity) {
      if (pq.getProductId() == product.getId()) {
        pq.addQuantity(1);
        return;
      }
    }
    productQuantity.add(new ProductQuantity(product, 1));
  }

  public void removeProduct(Product product) {
    for (ProductQuantity pq : productQuantity) {
      if (pq.getProductId() == product.getId()) {
        pq.subtractQuantity(1);
        if (pq.getQuantity() <= 0) {
          productQuantity.remove(pq);
        }
        return;
      }
    }
  }

  public void clearItems() {
    productQuantity.clear();
  }

  public int getId() {
    return this.id;
  }

  public List<ProductQuantity> getProductQuantity() {
    return this.productQuantity;
  }

  public void setId(int id) {
    this.id = id;
  }
}


