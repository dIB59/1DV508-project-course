package org.example.features.feedback;

import javafx.scene.control.Button;
import org.example.features.order.OrderService;
import java.sql.SQLException;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.example.shared.SceneRouter;

public class FeedbackController {
  @FXML private Button star1;
  @FXML private Button star2;
  @FXML private Button star3;
  @FXML private Button star4;
  @FXML private Button star5;

  private List<Button> stars;
  private int selectedRating = 0;


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
  private void initialize() { // Must be initialize, so it loads in
    stars = List.of(star1, star2, star3, star4, star5);

    for (int i = 0; i < stars.size(); i++) {
      final int index = i;
      Button star = stars.get(i);

      star.setOnMouseEntered(e -> highlightStars(index + 1));
      star.setOnMouseExited(e -> highlightStars(selectedRating));

      star.setOnAction(e -> {
        selectedRating = index + 1;
        highlightStars(selectedRating);
        orderService.setFeedback(selectedRating);
        goToPaymentPage();
      });
    }
  }

  private void highlightStars(int upTo) {
    for (int i = 0; i < stars.size(); i++) {
      if (i < upTo) {
        stars.get(i).setStyle("-fx-font-size: 24px; -fx-background-color: transparent; -fx-text-fill: gold;");
      } else {
        stars.get(i).setStyle("-fx-font-size: 24px; -fx-background-color: transparent; -fx-text-fill: #999;");
      }
    }
  }
}
