package org.example.features.coupons;

import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.example.shared.SceneRouter;

public class CouponsController {
  private final CouponsRepository couponsRepository;
  private final SceneRouter sceneRouter;
  @FXML private TextField codeField;
  @FXML private TextField discountField;

  public CouponsController(CouponsRepository couponsRepository, SceneRouter sceneRouter) {
    this.couponsRepository = couponsRepository;
    this.sceneRouter = sceneRouter;
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
}
