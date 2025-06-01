package org.example.features.coupons;

import java.sql.SQLException;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.example.features.translation.TranslationService;
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
      Button deleteButton = new Button("Delete coupon");
      deleteButton.setStyle(
          "-fx-background-color: -fx-color-destructive; " +
              "-fx-text-fill: white; " +
              "-fx-background-radius: 6; " +
              "-fx-padding: 6 14; " +
              "-fx-font-size: 13px;"
      );

      Label code = new Label(coupons.getCode());
      code.getProperties().put(TranslationService.DO_NOT_TRANSLATE, true);
      code.setStyle("-fx-text-fill: -fx-text-primary; -fx-font-size: 16px; -fx-font-weight: bold;");

      Label discount = new Label(String.format("%.0f%%", coupons.getDiscount() * 100));
      discount.setStyle("-fx-text-fill: -fx-text-secondary; -fx-font-size: 14px;");

      VBox coupon = new VBox(code, discount, deleteButton);
      coupon.setSpacing(8);
      coupon.setStyle(
          "-fx-padding: 14; " +
              "-fx-background-color: rgba(255,255,255,0.04); " +  // light contrast background for dark mode
              "-fx-background-radius: 8;"
      );

      deleteButton.setOnAction(event -> {
        try {
          couponsRepository.delete(coupons.getCode());
        } catch (SQLException e) {
          throw new RuntimeException(e);
        }
        couponsList.getChildren().remove(coupon);
      });

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
      salert();
      return;
    }

    try {
      couponsRepository.save(new Coupons(code, Double.parseDouble(discount) * 100));
    } catch (SQLException | NumberFormatException e) {
      throw new RuntimeException(e);
    }
    sceneRouter.refreshPage();
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

  public void salert() {
    // Show an alert if the cart is empty
    Alert alert = new Alert(Alert.AlertType.WARNING);
    alert.setTitle("Empty Cart");
    alert.setHeaderText(null);
    alert.setContentText("Your cart is empty. Please add items before proceeding.");
    alert.showAndWait();
  }

  public void goToProductsPage(ActionEvent actionEvent) {
    sceneRouter.goToDashboardPage();
  }

  public void goToLanguagesPage(ActionEvent actionEvent) {
    sceneRouter.goToLanguagesPage();
  }


  public void goToSettingsPage(ActionEvent actionEvent) {
    sceneRouter.goToSettingsPage();
  }

  public void goToHomePage(ActionEvent actionEvent) {
    sceneRouter.goToHomePage();
  }
}
