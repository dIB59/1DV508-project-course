package org.example.features.coupons;

public class Coupons {
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
    return discount;
  }
}
