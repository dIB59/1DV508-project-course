package org.example.features.payments;

import com.braintreegateway.*;

public class PaymentMethodService {
  private final BraintreeGateway gateway;

  public PaymentMethodService() {
    this.gateway = BraintreeConfig.getGateway();
  }

  public Result<? extends PaymentMethod> createPaymentMethod(String customerId, String nonce, boolean makeDefault) {
    PaymentMethodRequest request = new PaymentMethodRequest()
        .customerId(customerId)
        .paymentMethodNonce(nonce)
        .options()
        .makeDefault(makeDefault)
        .verifyCard(true)
        .failOnDuplicatePaymentMethod(true)
        .done();

    return gateway.paymentMethod().create(request);
  }

  public PaymentMethod getPaymentMethod(String token) {
    return gateway.paymentMethod().find(token);
  }

  public Result<? extends PaymentMethod> updatePaymentMethod(String token, String billingZip) {
    PaymentMethodRequest request = new PaymentMethodRequest()
        .billingAddress()
        .postalCode(billingZip)
        .done()
        .options()
        .verifyCard(true)
        .done();

    return gateway.paymentMethod().update(token, request);
  }
}
