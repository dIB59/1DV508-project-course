package org.example.features.payments;

public class FreePay implements PayStrategy {

  @Override
  public boolean payWithCard(CardPaymentDetails cardPaymentDetails) {
    return true;
  }
}
