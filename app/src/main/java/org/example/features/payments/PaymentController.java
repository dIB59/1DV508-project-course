package org.example.features.payments;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.example.features.order.OrderService;
import org.example.shared.SceneRouter;

public class PaymentController {

  private final SceneRouter sceneRouter;
  private final OrderService orderService;
  private Pay paymentService;

  @FXML private Button paypalButton;
  @FXML private Button freePayButton;
  @FXML private TextField cardholderNameField;
  @FXML private TextField cardNumberField;
  @FXML private TextField expirationField;
  @FXML private TextField cvvField;
  @FXML private TextField postalCodeField;

  @FXML private Label subtotalLabel;
  @FXML private Label subtotalValueLabel;
  @FXML private Label discountLabel;
  @FXML private Label discountValueLabel;
  @FXML private Label billedNowValueLabel;
  @FXML private Label footerNoteLabel;

  public PaymentController(SceneRouter sceneRouter, OrderService orderService, Pay paymentService) {
    this.sceneRouter = sceneRouter;
    this.orderService = orderService;
    this.paymentService = paymentService;
  }

  @FXML
  public void initialize() {
    paypalButton.setOnAction(this::handlePayPalPay);
    freePayButton.setOnAction(this::handleFreePay);

    populatePaymentDetails();
  }

  private void populatePaymentDetails() {
    // Assuming orderService has methods to get these values
    double subtotal = orderService.getSubtotal();
    double discount = orderService.getDiscountTotal();
    double billedNow = orderService.getTotal();
    String currencyCode = "USD"; // You might get this from a configuration
    double gbpConversionRate = 0.82; // Example conversion rate - should be dynamic in a real app
    double subtotalGBP = subtotal * gbpConversionRate;
    double discountGBP = discount * gbpConversionRate;
    double billedNowGBP = billedNow * gbpConversionRate;

    subtotalValueLabel.setText(String.format("$%.2f (approx. £%.2f GBP)", subtotal, subtotalGBP));
    discountValueLabel.setText(String.format("-$%.2f (approx. -£%.2f GBP)", discount, discountGBP));
    billedNowValueLabel.setText(String.format("%s $%.2f (approx. £%.2f GBP)", currencyCode, billedNow, billedNowGBP));
    footerNoteLabel.setText(String.format("All sales are charged in %s and all sales are final. You will be charged $%.2f %s", currencyCode, billedNow, currencyCode));

    // You can also conditionally set the text of labels like discountLabel
    if (discount <= 0) {
      discountLabel.setText("No discount applied:");
      discountValueLabel.setText("$0.00");
    }
  }

  private void handlePayPalPay(ActionEvent event) {
    try {
      paymentService = new PaypalPay();
      String cardNumber = cardNumberField.getText();
      String expiration = expirationField.getText();
      String cvv = cvvField.getText();

      CardPaymentDetails card = new CardPaymentDetails(cardNumber, expiration, cvv, orderService.getTotal());
      var success = paymentService.payWithCard(card);
      if (success) {
        orderService.setPaid();
        sceneRouter.goToReceiptPage();
      } else {
        // handle payment failure
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Payment Error");
        alert.setHeaderText("Payment Failed");
        alert.setContentText("There was an error processing your payment. Please try again.");
        alert.showAndWait();

      }
    } catch (Exception e) {
      // handle error
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Payment Error");
      alert.setHeaderText("Payment Failed");
      alert.setContentText("There was an error processing your payment. Please try again. "
          + e.getMessage());
      alert.showAndWait();
      e.printStackTrace();
    }
  }

  private void handleFreePay(ActionEvent event) {
    try {
      String cardNumber = cardNumberField.getText();
      String expiration = expirationField.getText();
      String cvv = cvvField.getText();

      CardPaymentDetails card = new CardPaymentDetails(cardNumber, expiration, cvv, orderService.getTotal());
      var success = paymentService.payWithCard(card);
      if (success) {
        orderService.setPaid();
        sceneRouter.goToReceiptPage();
      } else {
        // handle payment failure
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Payment Error");
        alert.setHeaderText("Payment Failed");
        alert.setContentText("There was an error processing your payment. Please try again.");
        alert.showAndWait();

      }

    } catch (Exception e) {
      // handle error
      e.printStackTrace();
    }
  }
}