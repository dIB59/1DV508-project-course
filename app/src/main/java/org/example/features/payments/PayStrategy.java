package org.example.features.payments;

public interface PayStrategy {
  boolean payWithCard(CardPaymentDetails cardPaymentDetails);
}
