package org.example.features.home;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.example.shared.SceneRouter;

public class HomeController {

  private final SceneRouter sceneRouter;
  private final HomeModel homeModel;
  @FXML
  public Label itemCountLabel;
  @FXML
  private Label welcomeLabel = new Label("Welcome to the Kiosk!");
  @FXML
  private Button checkoutButton;


  public HomeController(HomeModel homeModel, SceneRouter sceneRouter) {
    this.homeModel = homeModel;
    this.sceneRouter = sceneRouter;
  }

  @FXML
  public void gotToMenuPage() {
    sceneRouter.goToMenuPage();
  }

}
