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
    int rating = Integer.parseInt(clickedButton.getText()); // Assuming text is "1", "2", etc.
    // Save the rating to the database
    orderService.setFeedback(rating);
    goToPaymentPage();
  }
}
