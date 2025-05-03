package org.example.features.payments;

import com.braintreegateway.*;
import java.math.BigDecimal;

public class PaypalPay implements Pay {
  private final BraintreeGateway gateway;

  public PaypalPay() {
    this.gateway = BraintreeConfig.getGateway();
  }

  public boolean payWithCard(CardPaymentDetails cpDetails) {
    System.out.println("Processing payment with card number: " + cpDetails.cardNumber());
    System.out.println("Expiration date: " + cpDetails.expirationDate());
    System.out.println("CVV: " + cpDetails.cvv());
    System.out.println("Amount: " + cpDetails.amount());
    String expirationDate;
    //valdate card number
    if (cpDetails.expirationDate().matches("\\d{4}")) {
      expirationDate =
          cpDetails.expirationDate().substring(0, 2)
              + "/"
              + cpDetails.expirationDate().substring(2);
    } else {
      expirationDate = cpDetails.expirationDate();
    }

    TransactionRequest request =
        new TransactionRequest()
            .amount(cpDetails.amount())
            .creditCard()
            .number(cpDetails.cardNumber())
            .expirationDate(expirationDate)
            .cvv(cpDetails.cvv())
            .done()
            .options()
            .submitForSettlement(true)
            .done();

    Result<Transaction> result = gateway.transaction().sale(request);

    if (result.isSuccess()) {
      System.out.println("Transaction ID: " + result.getTarget().getId());
      printTransactionDetails(result.getTarget());
      return true;
    } else {
      if (result.getTransaction() != null) {
        System.err.println("Transaction failed: " + result.getTransaction().getProcessorResponseText());
      } else {
        for (ValidationError error : result.getErrors().getAllDeepValidationErrors()) {
          System.err.println("Validation Error: " + error.getMessage());
        }
      }
      return false;
    }
  }

  public void printTransactionDetails(Transaction transactionId) {
    try {
      Transaction transaction = gateway.transaction().find(transactionId.getId());
      System.out.println("Transaction ID: " + transaction.getId());
      System.out.println("Amount: " + transaction.getAmount());
      System.out.println("Status: " + transaction.getStatus());
      System.out.println("Created At: " + transaction.getCreatedAt());
      System.out.println("Updated At: " + transaction.getUpdatedAt());
      System.out.println("Customer ID: " + transaction.getCustomer().getId());

    } catch (Exception e) {
      System.err.println("Failed to retrieve transaction: " + e.getMessage());
    }
  }
}
