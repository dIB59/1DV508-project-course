package org.example.features.checkout;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.example.features.coupons.Coupons;
import org.example.features.coupons.CouponsRepository;
import org.example.features.order.OrderService;
import org.example.features.order.ProductQuantity;
import org.example.shared.SceneRouter;
import javafx.scene.layout.Region;

/** The type Checkout controller. */
public class CheckoutController implements Initializable {

  private final OrderService orderService;
  private final CouponsRepository couponsRepository;

  private final SceneRouter router;
  @FXML private Label itemCountLabel;
  @FXML private Label totalPriceLabel;
  @FXML private VBox itemListContainer; // Changed from ListView to VBox
  @FXML private TextField couponCodeField;

  /**
   * Instantiates a new Checkout controller.
   *
   * @param orderService the order service
   * @param sceneRouter the scene router
   */
  public CheckoutController(OrderService orderService, CouponsRepository couponsRepository, SceneRouter sceneRouter) {
    this.orderService = orderService;
    this.couponsRepository = couponsRepository;
    this.router = sceneRouter;
  }

  /** Goes to the receipt page. */
  public void goToReceiptPage() {
    if (orderService.getItems().isEmpty()) {
      // Show an alert if the cart is empty
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setTitle("Empty Cart");
      alert.setHeaderText(null);
      alert.setContentText("Your cart is empty. Please add items before proceeding.");
      alert.showAndWait();
      return;
    }
    router.goToReceiptPage();
  }

  /** Goes to the home/menu page. */
  public void goToMenuPage() {
    router.goToMenuPage();
  }

  /** Updates the displayed cart information (item count, list, and total price). */
  private void updateCartDisplay() {
    var items = orderService.getItems();
    int numberOfItems = items.stream().mapToInt(ProductQuantity::getQuantity).sum();

    itemCountLabel.setText(String.format("Items: %d", numberOfItems));

    // Clear the existing items before adding new ones
    itemListContainer.getChildren().clear();
    for (ProductQuantity item : items) {
      HBox itemBox = createItemBox(item);
      itemListContainer.getChildren().add(itemBox);
    }

    totalPriceLabel.setText(String.format("Total: $%.2f", orderService.getPrice()));
  }

  /**
   * Creates a VBox for each item with a label, remove button, and styling.
   *
   * @param item the product quantity
   * @return the HBox representing the item
   */
  private HBox createItemBox(ProductQuantity item) {
    HBox container = new HBox();
    container.setPadding(new Insets(15, 20, 15, 20));
    container.setStyle("-fx-background-color: white; -fx-border-radius: 8; -fx-border-color: #ddd;");
    container.setAlignment(Pos.TOP_LEFT);
    container.setPrefWidth(1080);

    // Create the label
    Label label = new Label(
        item.getProduct().getName()
            + " - $"
            + String.format("%.2f", item.getProduct().getPrice())
            + " x "
            + item.getQuantity());
    label.setFont(Font.font("Arial", 16));
    label.setTextFill(Color.valueOf("#333333"));
    label.setWrapText(true);

    // Create the buttons
    Button increaseButton = new Button("+");
    Button decreaseButton = new Button("-");

    // Style the buttons
    String buttonStyle = "-fx-background-color: #1E1EA9; -fx-text-fill: white; -fx-background-radius: 6; " +
        "-fx-cursor: hand; -fx-font-size: 14px; -fx-font-weight: bold;";
    increaseButton.setStyle(buttonStyle);
    decreaseButton.setStyle(buttonStyle);

    increaseButton.setPrefSize(30, 30);
    decreaseButton.setPrefSize(30, 30);

    increaseButton.setOnAction(event -> {
      orderService.addItem(item.getProduct());
      updateCartDisplay();
    });

    decreaseButton.setOnAction(event -> {
      orderService.removeItem(item.getProduct());
      updateCartDisplay();
    });

    // Right-aligned buttons
    HBox buttonBox = new HBox(5, decreaseButton, increaseButton);
    buttonBox.setAlignment(Pos.TOP_RIGHT);

    // Add spacing between label and buttons
    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);

    container.getChildren().addAll(label, spacer, buttonBox);
    return container;
  }
  public void applyCoupon() {
    String coupon = couponCodeField.getText();
    if (coupon == null || coupon.isEmpty()) {
      return;
    }
    Optional<Coupons> disc;

    try{
      disc = couponsRepository.findById(coupon);
    } catch (SQLException e)
    {
      throw new RuntimeException("Failed to get coupon from database");
    }
    disc.ifPresentOrElse(
        orderService::setDiscount,
        () -> couponNotFoundAlert().showAndWait()
    );
    totalPriceLabel.setText(String.format("Total: $%.2f", orderService.getPrice()));
  }

  private Alert couponNotFoundAlert() {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Coupon");
    alert.setContentText("Coupon not found");
    return alert;
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    int itemCount = orderService.getItems().size();
    itemCountLabel.setText("Items in cart: " + itemCount);

    // Populate ListView
    ObservableList<String> items = FXCollections.observableArrayList();
    for (ProductQuantity item : orderService.getItems()) {
      items.add(
          item.getProduct().getName()
              + " - $"
              + item.getProduct().getPrice()
              + " x "
              + item.getQuantity());
    }
    double totalPrice =
        orderService.getItems().stream().mapToDouble(ProductQuantity::getPrice).sum();
    totalPriceLabel.setText("Total Price: $" + String.format("%.2f", totalPrice));
    totalPriceLabel.setFont(Font.font("Arial", 20));
    totalPriceLabel.setTextFill(Color.valueOf("#16a085"));
    // display items in the VBox
    for (ProductQuantity item : orderService.getItems()) {
      HBox itemBox = createItemBox(item);
      itemListContainer.getChildren().add(itemBox);
    }
    updateCartDisplay();
  }
}
