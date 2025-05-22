package org.example.features.payments;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.example.features.order.OrderService;
import org.example.shared.SceneRouter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PaymentController {

  private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

  private final SceneRouter sceneRouter;
  private final OrderService orderService;
  private PayStrategy paymentService;

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

  private final String displayCurrency = "SEK";

  public PaymentController(SceneRouter sceneRouter, OrderService orderService, PayStrategy paymentService) {
    this.sceneRouter = sceneRouter;
    this.orderService = orderService;
    this.paymentService = paymentService;
  }

  @FXML
  public void initialize() {
    paypalButton.setOnAction(this::handlePayPalPay);
    freePayButton.setOnAction(this::handleFreePay);
    addSlashToExpirationField();
    addSpacesToCardNumberField();
    limitAndSanitizeCvvField();
    populatePaymentDetails();
  }

  private void limitAndSanitizeCvvField() {
    cvvField.textProperty().addListener((obs, oldText, newText) -> {
      String sanitized = newText.replaceAll("[^\\d]", "");
      if (!sanitized.equals(newText)) {
        cvvField.setText(sanitized);
      }
      if (sanitized.length() > 3) {
        cvvField.setText(sanitized.substring(0, 3));
      }
    });
  }

  private void addSpacesToCardNumberField() {
    cardNumberField.textProperty().addListener((obs, oldText, newText) -> {
      String digits = newText.replaceAll("\\D", "");

      // Limit to 16 digits
      if (digits.length() > 16) {
        digits = digits.substring(0, 16);
      }

      // Format into groups of 4 digits
      StringBuilder formatted = new StringBuilder();
      for (int i = 0; i < digits.length(); i++) {
        if (i > 0 && i % 4 == 0) {
          formatted.append(" ");
        }
        formatted.append(digits.charAt(i));
      }

      String formattedText = formatted.toString();

      // Avoid setting text if it's already formatted correctly
      if (!formattedText.equals(newText)) {
        int caretPos = cardNumberField.getCaretPosition();
        cardNumberField.setText(formattedText);
        // Adjust the caret position as best as possible
        cardNumberField.positionCaret(Math.min(caretPos, formattedText.length()));
      }
    });
  }



  private void addSlashToExpirationField() {
    expirationField.textProperty().addListener((obs, oldText, newText) -> {
      String sanitized = newText.replaceAll("[^\\d/]", "");
      if (!sanitized.equals(newText)) {
        expirationField.setText(sanitized);
        return;
      }

      if (sanitized.length() == 2 && !oldText.endsWith("/") && !sanitized.contains("/")) {
        expirationField.setText(sanitized + "/");
      } else if (sanitized.length() > 5) {
        expirationField.setText(sanitized.substring(0, 5));
      }
    });
  }

  private void populatePaymentDetails() {
    double subtotal = orderService.getSubtotal();
    double discount = orderService.getDiscountTotal();
    double total = orderService.getTotal();

    subtotalValueLabel.setText(formatCurrency(subtotal));
    discountValueLabel.setText(formatDiscount(discount));
    billedNowValueLabel.setText(formatCurrency(total));
    footerNoteLabel.setText(String.format("All sales are charged and processed in %s and all sales are final. You will be charged %s", displayCurrency, formatAmount(total)));

    if (discount <= 0) {
      discountLabel.setText("No discount applied:");
      discountValueLabel.setText(formatAmount(0.0));
    }
  }

  private String formatCurrency(double amount) {
    return String.format("%s %.2f", displayCurrency, amount);
  }

  private String formatAmount(double amount) {
    return String.format("%s %.2f", displayCurrency, amount);
  }

  private String formatDiscount(double discount) {
    String formattedAmount = formatCurrency(Math.abs(discount));
    return (discount < 0 ? "-" : "") + formattedAmount;
  }

  private void handlePayment(PayStrategy strategy) {
    try {
      String cardNumber = cardNumberField.getText().replace(" ", "");
      String expiration = expirationField.getText();
      String cvv = cvvField.getText();

      if (cardNumber.isEmpty() || expiration.isEmpty() || cvv.isEmpty()) {
        showAlert("Missing Information", "Please fill in all card details.");
        return;
      }

      CardPaymentDetails card = new CardPaymentDetails(cardNumber, expiration, cvv, orderService.getTotal());
      boolean success = strategy.payWithCard(card);

      if (success) {
        orderService.setPaid();
        if(orderService.getReceipt()){
          sceneRouter.goToReceiptPage();
        }

        else{
          sceneRouter.goToSmallReceiptPage();
        }
      } else {
        showAlert("Payment Failed", "There was an error processing your payment. Please try again.");
      }
    } catch (PaymentProcessingException e) {
      logger.error("Payment processing failed: {}", e.getMessage(), e);
      showAlert("Payment Failed", "There was an error processing your payment: " + e.getMessage());
    } catch (Exception e) {
      logger.error("An unexpected error occurred during payment: {}", e.getMessage(), e);
      showAlert("Unexpected Error", "An unexpected error occurred. Please try again.");
    }
  }

  @FXML
  private void handlePayPalPay(ActionEvent event) {
    paymentService = new PaypalPay();
    handlePayment(paymentService);
  }

  @FXML
  private void handleFreePay(ActionEvent event) {
    handlePayment(paymentService);
  }

  private void showAlert(String headerText, String contentText) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Payment Error");
    alert.setHeaderText(headerText);
    alert.setContentText(contentText);
    alert.showAndWait();
  }
}