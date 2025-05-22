package org.example.features.feedback;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.example.features.order.OrderService;
import org.example.shared.SceneRouter;

public class FeedbackController {
  private final SceneRouter sceneRouter;
  private final OrderService orderService;

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
    int rating = clickedButton.getParent()
        .getChildrenUnmodifiable()
        .indexOf(clickedButton) + 1;

    orderService.setFeedback(rating);
    goToPaymentPage();
  }
}
