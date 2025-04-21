package org.example.features.home;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.example.shared.SceneRouter;

/**
 * The type Home controller.
 */
public class HomeController {

  private final SceneRouter sceneRouter;
  private final HomeModel homeModel;
  /**
   * The Item count label.
   */
  @FXML
  public Label itemCountLabel;
  @FXML
  private Label welcomeLabel = new Label("Welcome to the Kiosk!");
  @FXML
  private Button checkoutButton;


  /**
   * Instantiates a new Home controller.
   *
   * @param homeModel   the home model
   * @param sceneRouter the scene router
   */
  public HomeController(HomeModel homeModel, SceneRouter sceneRouter) {
    this.homeModel = homeModel;
    this.sceneRouter = sceneRouter;
  }

  /**
   * Got to menu page.
   */
  @FXML
  public void gotToMenuPage() {
    sceneRouter.goToMenuPage();
  }

}
