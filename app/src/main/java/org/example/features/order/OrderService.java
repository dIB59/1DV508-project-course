package org.example.features.order;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.example.database.CrudRepository;
import org.example.features.coupons.Discount;
import org.example.features.ingredients.Ingredient;
import org.example.features.product.CustomizedProduct;
import org.example.features.product.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * OrderService class is responsible for managing the order. It provides methods to add, remove, and
 * clear items in the order, as well as saving the order to the database.
 */
public class OrderService {

  private static final Logger log = LoggerFactory.getLogger(OrderService.class);
  private final CrudRepository<Order, Integer> repository;
  private final List<CustomizedProduct> customizedProducts = new ArrayList<>();
  private Order order;

  /**
   * Instantiates a new Order service.
   *
   * @param orderRepository the order repository
   */
  public OrderService(CrudRepository<Order, Integer> orderRepository) {
    this.order = new Order();
    this.repository = orderRepository;
  }

  /**
   * Add item.
   *
   * @param product the item
   */
  public void addItem(Product product, Map<Ingredient, Integer> ingredientQuantities) {
    order.addProduct(product, ingredientQuantities);
  }

  // Sets member status to true
  public void setMember() {
    this.order.setMemberId(12345);
  }

  public boolean getMember() {
    return this.order.isMember();
  }

  public void setReceipt() {
    this.order.setReceipt(true);
  }

  public boolean getReceipt() {
    return this.order.isReceipt();
  }

  public Order.Type gettype() {
    return this.order.getType();
  }

  public int getId() {
    return this.order.getId();
  }

  public int getFeedback() {
    return this.order.getFeedback();
  }

  public void setFeedback(int feedback) {
    this.order.setFeedback(feedback);
  }

  public void setMemberID(int id) {
    this.order.setMemberId(id);
  }

  /**
   * Remove item.
   *
   * @param item the item
   */
  public void removeItem(Product item, Map<Ingredient, Integer> ingredientQuantities) {
    order.removeProduct(item, ingredientQuantities);
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
      var s = this.repository.save(order);
      this.clear();
      return s;
    } catch (Exception e) {
      log.error("Failed to save order: {}", e.getMessage());
      e.printStackTrace();
      throw new RuntimeException("Failed to save order");
    }
  }

  public void setDiscount(Discount discount) {
    order.setDiscount(discount);
  }

  public double getTotal() {
    return order.getPrice();
  }

  public void addCustomizedProduct(CustomizedProduct product) {
    this.customizedProducts.add(product);
  }

  public List<CustomizedProduct> getCustomizedProducts() {
    return customizedProducts;
  }

  public void setType(Order.Type ordertype) {
    order.setType(ordertype);
  }

  public void setPaid() {
    order.setPaid();
  }

  public double getSubtotal() {
    return order.getSubtotal();
  }

  public double getDiscountTotal() {
    return order.getSubtotal() - order.getPrice();
  }
}
