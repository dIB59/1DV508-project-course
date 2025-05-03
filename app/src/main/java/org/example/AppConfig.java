package org.example;

import io.github.cdimascio.dotenv.Dotenv;

public class AppConfig {
  private static final Dotenv dotenv = Dotenv.load();

  public static String getMerchantId() {
    return dotenv.get("MERCHANT_ID");
  }

  public static String getPayPalPrivateKey() {
    return dotenv.get("PAYPAL_PRIVATE_KEY");
  }

  public static String getPayPalPublicKey() {
    return dotenv.get("PAYPAL_PUBLIC_KEY");
  }
}
