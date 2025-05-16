package org.example.features.admin;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.example.shared.SceneRouter;

public class AdminController {

  private final SceneRouter sceneRouter;
  private final AdminRepository adminRepository;
  @FXML private TextField usernameField;
  @FXML private PasswordField passwordField;

  /**
   * Instantiates a new Admin controller.
   *
   * @param sceneRouter the scene router
   */
  public AdminController(SceneRouter sceneRouter, AdminRepository adminRepository) {
    this.sceneRouter = sceneRouter;
    this.adminRepository = adminRepository;
  }

  /** Go to admin page. */
  public void goToDashboard() {
    sceneRouter.goToDashboardPage();
  }

  public void handleLoginButtonAction(ActionEvent actionEvent) {
    goToDashboard();

    /*String username = usernameField.getText();
    String password = passwordField.getText();
    if (username.isEmpty() || password.isEmpty()) {
      usernameField.setText("Username or Password cannot be empty");
      passwordField.setText("");
      return;
      }
    if (validateCredentials(username, password)) {
      goToDashboard();
    } else {
      usernameField.setText("Invalid credentials");
      passwordField.setText("");
    }*/

  }

  private boolean validateCredentials(String username, String password) {
    return adminRepository
        .findByUsername(username)
        .map(admin -> admin.getPassword().equals(password))
        .orElse(false);
  }

  public void handleForgotPassword(MouseEvent mouseEvent) {
    Stage popupStage = new Stage(StageStyle.TRANSPARENT);
    popupStage.initModality(Modality.APPLICATION_MODAL);

    Label message = new Label("Sucks to be you");
    message.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-font-size: 24px; "
        + "-fx-padding: 30px; -fx-background-radius: 10;");

    StackPane root = new StackPane(message);
    root.setStyle("-fx-background-color: rgba(0, 0, 0, 1); -fx-padding: 40px; -fx-background-radius: 15;");

    Scene scene = new Scene(root, 400, 200);  // Set preferred width and height
    popupStage.setScene(scene);
    popupStage.setWidth(420);
    popupStage.setHeight(220);
    popupStage.centerOnScreen();
    popupStage.show();

    PauseTransition pause = new PauseTransition(Duration.seconds(1));
    pause.setOnFinished(event -> {
      popupStage.close();
      sceneRouter.goToHomePage();
    });
    pause.play();
  }


}
