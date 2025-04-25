package org.example.features.home;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.example.shared.SceneRouter;

/** The type Home controller. */
public class HomeController {

  private final SceneRouter sceneRouter;
  private final HomeModel homeModel;
  public boolean takeout = false;

  @FXML private Label welcomeLabel = new Label("Welcome to the Kiosk!");
  @FXML private Button eatInButton;
  @FXML private Button takeOutButton;

  /**
   * Instantiates a new Home controller.
   *
   * @param homeModel the home model
   * @param sceneRouter the scene router
   */
  public HomeController(HomeModel homeModel, SceneRouter sceneRouter) {
    this.homeModel = homeModel;
    this.sceneRouter = sceneRouter;
  }

  /** Got to menu page. */
  @FXML
  public void goToMenuPageTakeout() {
    sceneRouter.goToMenuPage();
    takeout = true;
  }

  @FXML
  public void goToMenuPageEatIn() {
    sceneRouter.goToMenuPage();
    takeout = false;
  }
}
