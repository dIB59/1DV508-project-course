package org.example.features.smallreceipt;

import javafx.animation.PauseTransition;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.util.Duration;
import org.example.features.order.OrderService;
import org.example.shared.SceneRouter;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class SmallReceiptController {
  private SceneRouter sceneRouter;
  private OrderService orderService;
  private PauseTransition autoRedirectPause;


  @FXML
  private Label titleLabel,
      orderIdLabel,
      EatinEatoutlabel;

  public SmallReceiptController(SceneRouter sceneRouter,  OrderService orderService) {
    this.sceneRouter = sceneRouter;
    this.orderService = orderService;
  }

  @FXML
  public void initialize() {
    EatinEatoutlabel.setText(orderService.gettype());
    orderIdLabel.setText("Order Number: " + orderService.getId());
    startAutoRedirect();
  }

  public void goToHomePage(){
    if (autoRedirectPause != null) {
      autoRedirectPause.stop();  // Stop the timer if still running
    }
    sceneRouter.goToHomePage();
  }

  private void startAutoRedirect() {
    autoRedirectPause = new PauseTransition(Duration.seconds(7));
    autoRedirectPause.setOnFinished(e -> goToHomePage());
    autoRedirectPause.play();
  }

}
