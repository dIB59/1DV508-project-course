package org.example.features.smallreceipt;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;
import org.example.features.order.OrderService;
import org.example.shared.SceneRouter;

public class SmallReceiptController {
  private final SceneRouter sceneRouter;
  private final OrderService orderService;
  private PauseTransition autoRedirectPause;

  @FXML private Label titleLabel, orderIdLabel, EatinEatoutlabel;

  public SmallReceiptController(SceneRouter sceneRouter, OrderService orderService) {
    this.sceneRouter = sceneRouter;
    this.orderService = orderService;
  }

  @FXML
  public void initialize() {
    EatinEatoutlabel.setText(orderService.gettype().name());
    orderIdLabel.setText("Order Number: " + orderService.getId());
    startAutoRedirect();
  }

  public void goToHomePage() {
    if (autoRedirectPause != null) {
      autoRedirectPause.stop(); // Stop the timer if still running
    }
    sceneRouter.goToHomePage();
  }

  private void startAutoRedirect() {
    autoRedirectPause = new PauseTransition(Duration.seconds(7));
    autoRedirectPause.setOnFinished(e -> goToHomePage());
    autoRedirectPause.play();
  }
}
