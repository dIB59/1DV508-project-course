package org.example.features.order;

import java.util.List;
import org.example.database.CrudRepository;
import org.example.features.product.Product;
import org.example.features.coupons.Discount;

/**
 * OrderService class is responsible for managing the order. It provides methods to add, remove, and
 * clear items in the order, as well as saving the order to the database.
 */
public class OrderService {

  private final CrudRepository<Order> repository;
  private Order order;

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
  public void clear() {
    this.order = new Order();
  }

  /**
   * Saves the order to the database. If an error occurs during saving, it prints an error message.
   */
  public Order saveOrderAndClear() {
    try {
      System.out.println("Order Unsaved: " + order);
      var s = this.repository.save(order);
      this.clear();
      System.out.println("Order saved successfully: " + s);

      return s;
    } catch (Exception e) {
      System.err.println("Error saving order: " + e.getLocalizedMessage());
      throw new RuntimeException("Failed to save order");
    }
  }

  public boolean setDiscount(Discount discount) {
    return order.setDiscount(discount);
  }

  public double getPrice() {
    return order.getPrice();
  }
}
