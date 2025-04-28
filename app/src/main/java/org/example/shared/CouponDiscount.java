package org.example.shared;

public class CouponDiscount implements Discount {

  private final String couponCode;
  private final double discountPercentage;

  public CouponDiscount(String couponCode, double discountPercentage) {
    this.couponCode = couponCode;
    this.discountPercentage = discountPercentage;
  }

  public String getCouponCode() {
    return couponCode;
  }

  @Override
  public double getDiscount() {
    return 0.01 * discountPercentage;
  }

}
