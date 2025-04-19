package org.example.features.home;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.example.shared.SceneRouter;

public class HomeController {

  @FXML private Label welcomeLabel = new Label("Welcome to the Kiosk!");
  @FXML public Label itemCountLabel;
  @FXML private Button checkoutButton;

  private final SceneRouter sceneRouter;
  private final HomeModel homeModel;


  public HomeController(HomeModel homeModel, SceneRouter sceneRouter) {
    this.homeModel = homeModel;
    this.sceneRouter = sceneRouter;
  }

  @FXML
  public void gotToMenuPage(ActionEvent actionEvent) {

    sceneRouter.goToMenuPage();
  }

}
