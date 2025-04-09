package org.example.features.checkout;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.example.router.SceneRouter;

public class CheckoutController {

  @FXML private Label itemCountLabel;
  @FXML private Button homeButton;

  public void goToHomePage(ActionEvent actionEvent) {
    SceneRouter.goToHomePage();
  }
}
