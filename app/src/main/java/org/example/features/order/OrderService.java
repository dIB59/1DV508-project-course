package org.example.features.order;

import java.util.List;
import org.example.features.product.Product;
import org.example.shared.CrudRepository;


public class OrderService {

  private final Order order;
  private final CrudRepository<Order> repository;

  public OrderService(OrderRepository orderRepository) {
    this.order = new Order();
    this.repository = orderRepository;
  }

  public void addItem(Product item) {
    this.order.addProduct(item);
  }

  public void removeItem(Product item) {
    this.order.removeProduct(item);
  }

  public List<ProductQuantity> getItems() {
    return order.getProductQuantity();
  }

  public void clearItems() {
    this.order.clearItems();
  }

  public void saveOrder() {
    try {
      this.repository.save(order);
    } catch (Exception e) {
      System.err.println("Error saving order: " + e.getLocalizedMessage());
    }
  }

}
