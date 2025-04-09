package org.example.features.home;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.example.router.SceneRouter;

public class HomeController {

  @FXML private Label welcomeLabel;
  @FXML public Label itemCountLabel;
  @FXML private Button checkoutButton;

  private final HomeModel model = new HomeModel();

  @FXML
  public void initialize() {
    welcomeLabel.setText(model.getWelcomeMessage());
  }

  @FXML
  public void goToCheckoutPage(ActionEvent actionEvent) {
    System.out.println("Checkout button clicked");

    checkoutButton.setStyle(
        "-fx-background-color: #FF0000; -fx-text-fill: #FFFFFF; -fx-font-size: 16px;");
    SceneRouter.goToCheckoutPage();
  }

}
