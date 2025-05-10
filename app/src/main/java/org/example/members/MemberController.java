package org.example.members;

import com.sun.javafx.scene.control.IntegerField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.features.admin.AdminRepository;
import org.example.features.order.OrderService;
import org.example.shared.SceneRouter;
import org.w3c.dom.Text;

import java.sql.SQLException;

public class MemberController {
  private OrderService orderService;
  private final SceneRouter sceneRouter;
  private final MemberRepository memberRepository;
  @FXML
  Label MemberLoginLabel;
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

  public void handleLoginButtonAction(ActionEvent actionEvent) throws SQLException {

    String input = personalnumber.getText();

    if (input == null || !input.matches("\\d+")) { // matches("\\d+") ensures it's all digits.
      MemberLoginLabel.setText("Please enter a valid personal number");
      return;
    }

    Integer number = Integer.parseInt(input); // converts string to int

    if (number < 1) {
      MemberLoginLabel.setText("Please enter a valid personal number");
      return;
    }

    if (validateCredentials(number)) {
      this.orderService.setMember();
      goToPaymentPage();
    } else {
      MemberLoginLabel.setText("Please enter correct personal number");
    }
  }

  private boolean validateCredentials(Integer personalnumber) throws SQLException {
    return memberRepository
        .findById(personalnumber)
        .isPresent();
  }
  
}
