package org.example.features.feedback;

import javafx.scene.control.Button;
import org.example.features.order.OrderService;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.example.shared.SceneRouter;

public class FeedbackController {
  private final SceneRouter sceneRouter;
  private OrderService orderService;

  public  FeedbackController(SceneRouter sceneRouter, OrderService orderService) {
    this.sceneRouter = sceneRouter;
    this.orderService = orderService;
  }

  public void goToPaymentPage() {
    sceneRouter.goToPaymentPage();
  }


  @FXML
  private void handleRating(ActionEvent event) {
    Button clickedButton = (Button) event.getSource();

    // Get parent HBox and find index of clicked button
    int rating = ((javafx.scene.layout.HBox) clickedButton.getParent())
        .getChildrenUnmodifiable()
        .indexOf(clickedButton) + 1;

    orderService.setFeedback(rating);
    goToPaymentPage();
  }
}
