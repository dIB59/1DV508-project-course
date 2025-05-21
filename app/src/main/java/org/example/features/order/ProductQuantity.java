package org.example.features.order;

import org.example.features.product.CustomizedProduct;
import org.example.features.product.Product;

/**
 * ProductQuantity class represents a product and its quantity in an order. It
 * contains methods to
 * add or subtract the quantity of the product.
 *
 * <p>
 * The reason is that product is stored within the database, and the quantity is
 * not
 */
public class ProductQuantity {

  private final CustomizedProduct customizedProduct;
  private int quantity;

  /**
   * Instantiates a new Product quantity.
   *
   * @param product  the product
   * @param quantity the quantity
   */
  public ProductQuantity(CustomizedProduct customizedProduct, int quantity) {
    this.customizedProduct = customizedProduct;
    this.quantity = quantity;
  }

  public CustomizedProduct getCustomizedProduct() {
    return customizedProduct;
  }

  /**
   * Add quantity.
   *
   * @param quantity the quantity
   */
  public void addQuantity(int quantity) {
    this.quantity += quantity;
  }

  /**
   * Subtract quantity.
   *
   * @param quantity the quantity
   */
  public void subtractQuantity(int quantity) {
    this.quantity -= quantity;
  }

  /**
   * Gets product id.
   *
   * @return the product id
   */
  public int getProductId() {
    return customizedProduct.getProduct().getId();
  }

  /**
   * Gets price.
   *
   * @return the price
   */
  public double getPrice() {
    return customizedProduct.getTotalPrice() * quantity;
  }

  /**
   * Gets quantity.
   *
   * @return the quantity
   */
  public int getQuantity() {
    return quantity;
  }

  /**
   * Gets product.
   *
   * @return the product
   */

  @Override
  public String toString() {
    return "ProductQuantity{" + "product=" + customizedProduct.getProduct().getName() + ", quantity=" + quantity + '}';
  }
}
