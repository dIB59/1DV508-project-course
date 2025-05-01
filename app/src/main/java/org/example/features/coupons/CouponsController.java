package org.example.features.coupons;

import java.sql.SQLException;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.example.features.product.Product;
import org.example.shared.SceneRouter;

public class CouponsController {
  private final CouponsRepository couponsRepository;
  private final SceneRouter sceneRouter;
  @FXML private TextField codeField;
  @FXML private TextField discountField;
  @FXML private VBox couponsList;

  public CouponsController(CouponsRepository couponsRepository, SceneRouter sceneRouter) {
    this.couponsRepository = couponsRepository;
    this.sceneRouter = sceneRouter;
  }

  /** Initialize. */
  public void initialize() {
    for (Coupons coupons : getCouponsList()) {
      Button addButton = new Button("Delete coupon");

      Label code = new Label(coupons.getCode());
      Label discount = new Label(String.format("$%.2f", coupons.getDiscount()));

      VBox coupon = new VBox(code, discount, addButton);
      coupon.setSpacing(5);
      coupon.setStyle("-fx-padding: 10; -fx-border-color: #ccc; -fx-border-radius: 5;");

      // addButton.setOnAction(event -> orderService.addItem(product));
      couponsList.getChildren().add(coupon);
    }
  }
  /**
   * Go to dashboard page.
   *
   * <p>This method is called when the user clicks the "Go to Dashboard" button.
   */
  public void handleAddCouponButtonAction() {
    String code = codeField.getText();
    String discount = discountField.getText();

    if (code.isEmpty() || discount.isEmpty()) {
      codeField.setText("Code or Discount cannot be empty");
      discountField.setText("");
      return;
    }

    try {
      couponsRepository.save(new Coupons(code, Double.parseDouble(discount)* 100));
    } catch (SQLException | NumberFormatException e) {
      throw new RuntimeException(e);
    }
    sceneRouter.goToDashboardPage();
  }

  public void handleDeleteCouponButtonAction() {
    String code = codeField.getText();
    if (code.isEmpty()) {
      codeField.setText("Code cannot be empty");
      return;
    }

    try {
      couponsRepository.delete(code);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    sceneRouter.goToDashboardPage();
  }

  public List<Coupons> getCouponsList() {
    List<Coupons> coupons;
    try {
      coupons = couponsRepository.findAll();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return coupons;
  }

  public void goToDashboardPage(ActionEvent event) {
    sceneRouter.goToDashboardPage();
  }
}
