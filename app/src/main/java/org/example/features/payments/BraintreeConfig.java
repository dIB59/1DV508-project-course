package org.example.features.payments;
import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Environment;

public class BraintreeConfig {
  private static final BraintreeGateway gateway = new BraintreeGateway(
      Environment.SANDBOX, // Use PRODUCTION in real deployments
      "",
      "",
      ""
  );

  public static BraintreeGateway getGateway() {
    return gateway;
  }
}
