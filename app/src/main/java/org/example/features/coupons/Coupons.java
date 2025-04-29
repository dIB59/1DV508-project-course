package org.example.features.coupons;
import org.example.features.product.ProductDetailsController;

public class Coupons implements Discount {
  private final String code;
  private final double discount;
  public Coupons(String code, double discount) {
    this.code = code;
    this.discount = discount;

  }

  public String getCode() {
    return code;
  }

  public double getDiscount() {
    return 0.01 * discount;
  }

  public void setDiscount(double discount) {
  }

  public void setCode(String code) {
  }
}
