package org.example.features.payments;

import java.math.BigDecimal;

public class FreePay implements Pay{

  @Override
  public boolean payWithCard(CardPaymentDetails cardPaymentDetails) {
    return true;
  }
}
