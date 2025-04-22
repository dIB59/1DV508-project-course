package org.example.features.order;

import java.util.List;
import org.example.features.product.Product;
import org.example.shared.CrudRepository;

/**
 * OrderService class is responsible for managing the order. It provides methods to add, remove, and
 * clear items in the order, as well as saving the order to the database.
 */
public class OrderService {

  private final Order order;
  private final CrudRepository<Order> repository;

  /**
   * Instantiates a new Order service.
   *
   * @param orderRepository the order repository
   */
  public OrderService(CrudRepository<Order> orderRepository) {
    this.order = new Order();
    this.repository = orderRepository;
  }

  /**
   * Add item.
   *
   * @param item the item
   */
  public void addItem(Product item) {
    this.order.addProduct(item);
  }

  /**
   * Remove item.
   *
   * @param item the item
   */
  public void removeItem(Product item) {
    this.order.removeProduct(item);
  }

  /**
   * Gets items.
   *
   * @return the items
   */
  public List<ProductQuantity> getItems() {
    return order.getProductQuantity();
  }

  /** Clear items. */
  public void clearItems() {
    this.order.clearItems();
  }

  /**
   * Saves the order to the database. If an error occurs during saving, it prints an error message.
   */
  public void saveOrder() {
    try {
      this.repository.save(order);
    } catch (Exception e) {
      System.err.println("Error saving order: " + e.getLocalizedMessage());
    }
  }
}
