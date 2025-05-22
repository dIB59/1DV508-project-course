package org.example.features.coupons;

import org.example.database.Identifiable;

public class Coupons implements Discount, Identifiable<String> {
  private final String code;
  private final double discount;

  public Coupons(String code, double discount) {
    this.code = code;
    this.discount = discount;
  }

  public String getCode() {
    return code;
  }

  public String getId() {
    return code;
  }

  public double getDiscount() {
    return 0.01 * discount;
  }
}
