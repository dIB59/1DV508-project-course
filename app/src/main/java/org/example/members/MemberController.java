package org.example.members;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.features.admin.AdminRepository;
import org.example.features.order.OrderService;
import org.example.shared.SceneRouter;

import java.sql.SQLException;

public class MemberController {
  private OrderService orderService;
  private final SceneRouter sceneRouter;
  private final MemberRepository memberRepository;
  @FXML
  private TextField personalnumber;


  /**
   * Instantiates a new Member controller.
   *
   * @param sceneRouter the scene router
   */
  public MemberController(SceneRouter sceneRouter, MemberRepository memberRepository, OrderService orderService) {
    this.sceneRouter = sceneRouter;
    this.memberRepository = memberRepository;
    this.orderService = orderService;
  }

  public void goToPaymentPage() {
    sceneRouter.goToPaymentPage();
  }

  public void handleNonMemberLoginButtionAction(ActionEvent actionevent){
    goToPaymentPage();
  }

  public void handleLoginButtonAction(ActionEvent actionEvent) {
    this.orderService.setMember();
    goToPaymentPage();

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

  private boolean validateCredentials(Integer personalnumber) throws SQLException {
    return memberRepository
        .findById(personalnumber)
        .isPresent();
  }
}
