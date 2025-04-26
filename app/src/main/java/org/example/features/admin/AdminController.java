package org.example.features.admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
    String username = usernameField.getText();
    String password = passwordField.getText();
    if (username.isEmpty() || password.isEmpty()) {
      usernameField.setText("Username or Password cannot be empty");
      passwordField.setText("");
    } else {
      validateCredentials(username, password);
    }
  }

  private void validateCredentials(String username, String password) {
    this.adminRepository.findByUsername(username)
        .ifPresentOrElse(admin -> {
              if (admin.getPassword().equals(password)) {
                goToDashboard();
              }
            },
            () -> {
              usernameField.setText("Invalid username or password");
              passwordField.setText("");
            });
  }
}
