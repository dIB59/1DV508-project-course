package org.example.features.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.example.database.Identifiable;
import org.example.features.coupons.Discount;
import org.example.features.ingredients.Ingredient;
import org.example.features.product.CustomizedProduct;
import org.example.features.product.Product;

/**
 * Order class represents a customer's order in the system. It contains a list of ProductQuantity
 * objects, each representing a product and its quantity.
 */
public class Order implements Identifiable<Integer> {
  private final List<ProductQuantity> productQuantity;
  private final LocalDateTime createdAt;
  private int id;
  private Optional<Discount> discount;
  private int feedback;
  private Optional<Integer> memberID;
  private boolean isReceipt;
  private Type type;
  private boolean isPaid;

  public Order(
      int id,
      List<ProductQuantity> productQuantities,
      Optional<Discount> discount,
      int feedback,
      Optional<Integer> memberID,
      boolean isReceipt,
      Type type,
      boolean isPaid,
      LocalDateTime createdAt) {
    this.id = id;
    this.productQuantity = productQuantities;
    this.discount = discount;
    this.feedback = feedback;
    this.memberID = memberID;
    this.isReceipt = isReceipt;
    this.type = type;
    this.isPaid = isPaid;
    this.createdAt = createdAt;
  }

  /** Default constructor. */
  public Order() {
    this.id = 0;
    this.productQuantity = new ArrayList<>();
    this.discount = Optional.empty(); // Or Optional.of(new Coupons("No Discount", 0)) if needed
    this.feedback = 0;
    this.memberID = Optional.empty();
    this.isReceipt = true;
    this.type = Type.EAT_IN;
    this.isPaid = false;
    this.createdAt = LocalDateTime.now();
  }

  public boolean isMember() {
    return memberID.isPresent();
  }

  /**
   * Adds a product to the order. If the product already exists in the order, it increments the
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
   * Removes a product from the order. If the quantity of the product becomes zero or less, it
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

  public Optional<Integer> getMemberId() {
    return memberID;
  }

  public void setMemberId(int id) {
    this.memberID = Optional.of(id);
  }

  public boolean isReceipt() {
    return isReceipt;
  }

  public void setReceipt(boolean receipt) {
    isReceipt = receipt;
  }

  public int getFeedback() {
    return this.feedback;
  }

  public void setFeedback(int feedback) {
    this.feedback = feedback;
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
    sb.append(discount.map(Discount::getCode).orElse("No Discount"));
    sb.append("\n");
    sb.append("Order ID: ").append(id).append("\n");
    for (ProductQuantity pq : productQuantity) {
      sb.append(pq.toString());
    }
    return sb.toString();
  }

  public Optional<Discount> getDiscount() {
    return this.discount;
  }

  public void setDiscount(Discount discount) {
    this.discount = Optional.of(discount);
  }

  public double getPrice() {
    return productQuantity.stream()
        .mapToDouble(ProductQuantity::getPrice)
        .map(price -> discount.map(d -> d.applyDiscount(price)).orElse(price))
        .sum();
  }

  public double getSubtotal() {
    return productQuantity.stream().mapToDouble(ProductQuantity::getPrice).sum();
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

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public enum Type {
    TAKEAWAY,
    EAT_IN
  }
}
