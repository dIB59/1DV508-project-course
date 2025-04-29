package org.example.features.admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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
}
