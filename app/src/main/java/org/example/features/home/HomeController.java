package org.example.features.home;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.example.router.SceneRouter;

public class HomeController {

  private final Label welcomeLabel = new Label("Welcome to the Kiosk App!");

  private final HomeModel model = new HomeModel();

  @FXML
  public void initialize() {
    welcomeLabel.setText(model.getWelcomeMessage());
  }

  public void handleContinue(ActionEvent actionEvent) {
    // Handle the continue button action
    System.out.println("Continue button clicked");
  }

  public void goToCheckoutPage(ActionEvent actionEvent) {
    // Handle the checkout button action
    System.out.println("Checkout button clicked");
    SceneRouter.goTo(SceneRouter.KioskPage.CHECKOUT);
    switch (SceneRouter.getCurrentPage()) {
      case CHECKOUT -> System.out.println("Navigated to Checkout page");
      case DASHBOARD -> System.out.println("Navigated to Dashboard page");
      case HOME -> System.out.println("Navigated to Home page");
      case ORDER -> System.out.println("Navigated to Order page");
    }
  }

}
