package org.example.features.payments;

/**
 * Interface for payment strategies.
 * This interface defines a method for processing payments with a card.
 * Usage:
 *
 * <pre>
 *   Pay payStrategy = new PaypalPay();
 *   CardPaymentDetails cardDetails =
 *   <p></p>
 *   new CardPaymentDetails("1234567890123456", "12/25", "123", 100.00);
 *   boolean success = payStrategy.payWithCard(cardPaymentDetails);
 *   if (success) {
 *   log.info("Payment successful");
 *   } else {
 *   log.info("Payment failed");
 *   }
 */
public interface PayStrategy {
  boolean payWithCard(CardPaymentDetails cardPaymentDetails);
}
