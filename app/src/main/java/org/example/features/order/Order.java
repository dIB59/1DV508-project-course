package org.example.features.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.example.database.Identifiable;
import org.example.features.coupons.Coupons;
import org.example.features.coupons.Discount;
import org.example.features.ingredients.Ingredient;
import org.example.features.product.CustomizedProduct;
import org.example.features.product.Product;

/**
 * Order class represents a customer's order in the system. It contains a list
 * of ProductQuantity
 * objects, each representing a product and its quantity.
 */
public class Order implements Identifiable<Integer> {

  private final List<ProductQuantity> productQuantity;
  private Discount discount = new Coupons("No Discount", 0);
  private int id;
  private int feedback;
  private boolean isMember;
  private int memberID;
  private boolean isReceipt;
  private Type type;
  private boolean isPaid = false;

  /**
   * Instantiates a new Order.
   *
   * @param id              the id
   * @param productQuantity the product quantity
   */
  public Order(int id, List<ProductQuantity> productQuantity) {
    this.id = id;
    this.productQuantity = productQuantity;
    isMember = false;
    isReceipt = true;
  }

  /** Instantiates a new Order. */
  public Order() {
    this.id = 0;
    this.productQuantity = new ArrayList<>();
    isMember = false;
  }

  public void setMember() {
    this.isMember = true;
  }

  public boolean getMember() {
    return this.isMember;
  }

  public void setReceipt() {
    this.isReceipt = true;
  }

  public boolean getReceipt() {
    return this.isReceipt;
  }

  public void setMemberID(int memberId) {
    this.memberID = memberId;
    this.isMember = true;
  }

  public int getMemberID() {
    return this.memberID;
  }

  public void setMemberDB(boolean getMember) {
    if (getMember) {
      this.setMember();
    }
  }

  /**
   * Adds a product to the order. If the product already exists in the order, it
   * increments the
   * quantity by 1.
   *
   * @param product The product to be added.
   */
  public void addProduct(Product product, Map<Ingredient, Integer> ingredientQuantities) {
    CustomizedProduct cp = new CustomizedProduct(product, ingredientQuantities);
    for (ProductQuantity pq : productQuantity) {
      if (pq.getCustomizedProduct().equals(cp)) {
        pq.addQuantity(1);
        return;
      }
    }
    productQuantity.add(new ProductQuantity(cp, 1));
  }

  /**
   * Removes a product from the order. If the quantity of the product becomes zero
   * or less, it
   * removes the product from the order.
   *
   * @param product The product to be removed.
   */
  public void removeProduct(Product product, Map<Ingredient, Integer> ingredientQuantities) {
    CustomizedProduct cp = new CustomizedProduct(product, ingredientQuantities);

    for (ProductQuantity pq : productQuantity) {
      if (pq.getCustomizedProduct().equals(cp)) {
        pq.subtractQuantity(1);
        if (pq.getQuantity() <= 0) {
          productQuantity.remove(pq);
        }
        return;
      }
    }
  }

  /** Clears all items from the order. */
  public void clearItems() {
    productQuantity.clear();
  }

  /**
   * Returns the ID of the order.
   *
   * @return the id
   */
  public Integer getId() {
    return this.id;
  }

  /**
   * Sets the ID of the order.
   *
   * @param id The new ID of the order.
   */
  public void setId(int id) {
    this.id = id;
  }

  public void setFeedback(int feedback) {
    this.feedback = feedback;
  }

  public  int getFeedback() {
    return this.feedback;
  }
  /**
   * Returns the list of ProductQuantity objects in the order.
   *
   * @return The list of ProductQuantity objects.
   */
  public List<ProductQuantity> getProductQuantity() {
    return this.productQuantity;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(discount.getCode());
    sb.append("\n");
    sb.append("Order ID: ").append(id).append("\n");
    for (ProductQuantity pq : productQuantity) {
      sb.append(pq.toString());
    }
    return sb.toString();
  }

  public void setDiscount(Discount discount) {
    this.discount = discount;
  }

  public Discount getDiscount() {
    return this.discount;
  }

  public double getPrice() {
    return productQuantity.stream()
        .mapToDouble(ProductQuantity::getPrice)
        .map(discount::applyDiscount)
        .sum();
  }

  public double getSubtotal() {
    return productQuantity.stream()
        .mapToDouble(ProductQuantity::getPrice)
        .sum();
  }

  public BigDecimal getPriceBigDecimal() {
    return BigDecimal.valueOf(getPrice());
  }


  public boolean isPaid() {
    return isPaid;
  }

  public void setPaid() {
    this.isPaid = true;
  }

  public Type getType() {
    return type;
  }

  public void setType(Type type) {
    this.type = type;
  }

  public enum Type {
    TAKEOUT,
    EAT_IN
  }
}
