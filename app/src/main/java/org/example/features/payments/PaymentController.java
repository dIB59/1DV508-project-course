package org.example.features.payments;

import java.math.BigDecimal;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.example.features.order.OrderService;
import org.example.shared.SceneRouter;

public class PaymentController {

  @FXML private VBox buttonContainer;
  @FXML private TextField cardNumberField;
  @FXML private TextField expirationField;
  @FXML private TextField cvvField;
  @FXML private Label statusLabel;

  private final SceneRouter sceneRouter;
  private final OrderService orderService;
  private Pay paymentService;

  public PaymentController(SceneRouter sceneRouter, OrderService orderService, Pay paymentService) {
    this.sceneRouter = sceneRouter;
    this.orderService = orderService;
    this.paymentService = paymentService;
  }

  @FXML
  private void onPayClicked() {
    String cardNumber = cardNumberField.getText().trim();
    String expiration = expirationField.getText().trim();
    String cvv = cvvField.getText().trim();

    if (cardNumber.isEmpty() || expiration.isEmpty() || cvv.isEmpty()) {
      statusLabel.setText("Please fill in all fields.");
      statusLabel.setTextFill(javafx.scene.paint.Color.RED);
      return;
    }

    CardPaymentDetails paymentDetails =
        new CardPaymentDetails(cardNumber, expiration, cvv, orderService.getPrice());

    boolean success = paymentService.payWithCard(paymentDetails);
    if (success) {
      statusLabel.setText("Payment successful!");
      statusLabel.setTextFill(javafx.scene.paint.Color.GREEN);
      orderService.setPaid();
      createGoToReceipt();
    } else {
      statusLabel.setText("Payment failed.");
      statusLabel.setTextFill(javafx.scene.paint.Color.RED);
      createGotoHomeButton();
    }
  }

  void createGotoHomeButton() {
    var button = new Button("Go to Home");
    button.setOnAction(event -> sceneRouter.goToHomePage());
    button.setVisible(true);
    button.setStyle("-fx-background-color: #8e2c2c; -fx-text-fill: #FFFFFF;");
    button.setPrefSize(100, 50);
    buttonContainer.getChildren().clear();
    buttonContainer.getChildren().add(button);

  }

  void createGoToReceipt() {
    var button = new Button("Get Receipt");
    button.setOnAction(event -> sceneRouter.goToReceiptPage());
    button.setVisible(true);
    button.setStyle("-fx-background-color: #FF0000; -fx-text-fill: #FFFFFF;");
    button.setPrefSize(100, 50);
    buttonContainer.getChildren().clear();
    buttonContainer.getChildren().add(button);
  }
}
