package org.example.features.order;

import java.util.ArrayList;
import java.util.List;
import org.example.database.CrudRepository;
import org.example.features.product.CustomizedProduct;
import org.example.features.product.Product;
import org.example.features.coupons.Discount;

/**
 * OrderService class is responsible for managing the order. It provides methods to add, remove, and
 * clear items in the order, as well as saving the order to the database.
 */
public class OrderService {

  private final CrudRepository<Order, Integer> repository;
  private Order order;
  private List<CustomizedProduct> customizedProducts = new ArrayList<>();
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
   * @param item the item
   */
  public void addItem(Product item) {
    this.order.addProduct(item);
  }

  // Sets member status to true
  public void setMember(){
    this.order.setMember();
  }

  public boolean getMember(){
    return this.order.getMember();
  }

  public void setMemberDB(boolean getMember){
    if (getMember){
      this.order.setMember();
    }
  }

  public void setMemberID(int id){
    this.order.setMemberID(id);
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
      var s = this.repository.save(order);
      s.setDiscount(order.getDiscount());
      s.setMemberDB(order.getMember());
      s.settype(order.gettype());
      s.setMemberID(order.getMemberID());
      this.clear();
      return s;
    } catch (Exception e) {
      System.err.println("Error saving order: " + e.getLocalizedMessage());
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

  public void settype(String ordertype) {
    order.settype(ordertype);
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
