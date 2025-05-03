package org.example.features.payments;

import java.math.BigDecimal;

public interface Pay {
  boolean payWithCard(CardPaymentDetails cardPaymentDetails);
}
