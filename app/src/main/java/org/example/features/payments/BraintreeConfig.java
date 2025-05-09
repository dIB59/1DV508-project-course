package org.example.features.payments;
import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Environment;
import org.example.AppConfig;

public class BraintreeConfig {
  private static final BraintreeGateway gateway = new BraintreeGateway(
      Environment.SANDBOX, // Use PRODUCTION in real deployments
      AppConfig.getMerchantId(),
      AppConfig.getPayPalPublicKey(),
      AppConfig.getPayPalPrivateKey()
  );

  public static BraintreeGateway getGateway() {
    return gateway;
  }
}
