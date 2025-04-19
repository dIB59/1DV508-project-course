package org.example.features.order;

import java.util.ArrayList;
import java.util.List;
import org.example.features.menu.Product;


public class OrderService {

  private final List<Product> products;

  public OrderService() {
    products = new ArrayList<>();
  }

  public void addItem(Product item) {
    products.add(item);
  }

  public void removeItem(Product item) {
    products.remove(item);
  }

  public List<Product> getItems() {
    return products;
  }

  public void clearItems() {
    products.clear();
  }
}
