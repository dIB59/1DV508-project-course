package org.example.features.home;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

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
}
