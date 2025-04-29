package org.example.features.coupons;

public interface Discount {

  double getDiscount();

  default double applyDiscount(double price) {
    return price * (1 - getDiscount());
  }
}
