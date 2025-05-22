package org.example.features.payments;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record CardPaymentDetails(
    String cardNumber, String expirationDate, String cvv, BigDecimal amount) {
  public CardPaymentDetails {
    if (cardNumber == null || cardNumber.isEmpty()) {
      throw new IllegalArgumentException("Card number cannot be null or empty");
    }
    if (expirationDate == null || expirationDate.isEmpty()) {
      throw new IllegalArgumentException("Expiration date cannot be null or empty");
    }
    if (cvv == null || cvv.isEmpty()) {
      throw new IllegalArgumentException("CVV cannot be null or empty");
    }
    if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Amount must be greater than zero");
    }
  }

  public CardPaymentDetails(String cardNumber, String expirationDate, String cvv, double amount) {
    this(
        cardNumber,
        expirationDate,
        cvv,
        BigDecimal.valueOf(amount).setScale(2, RoundingMode.HALF_UP));
  }
}
