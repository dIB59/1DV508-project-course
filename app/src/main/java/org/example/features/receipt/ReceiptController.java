package org.example.features.receipt;

import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.example.features.order.Order;
import org.example.features.order.ProductQuantity;
import org.example.features.product.Product;
import org.example.features.coupons.Coupons;
import org.example.shared.SceneRouter;

public class ReceiptController {

  private final Order order;
  private final SceneRouter sceneRouter;
  @FXML public Label orderIdLabel;
  @FXML private VBox itemsContainer;
  @FXML
  private Label titleLabel,
      totalLabel,
      couponsLabel,
      thankYouLabel,
      restaurantNameLabel,
      addressLabel,
      contactLabel;

  public ReceiptController(Order order, SceneRouter sceneRouter) {
    this.order = order;
    System.out.println("ReceiptController: " + order);
    this.sceneRouter = sceneRouter;
  }

  @FXML
  public void initialize() {
    List<ProductQuantity> productQuantities = order.getProductQuantity();
    double total = 0.0;

    for (ProductQuantity pq : productQuantities) {
      Product product = pq.getProduct();
      int quantity = pq.getQuantity();
      double itemTotal = product.price() * quantity;
      total += itemTotal;

      // Left: Product name with quantity
      Label nameLabel = new Label(product.name() + " x" + quantity);
      nameLabel.getStyleClass().add("item-name");

      // Right: Price
      Label priceLabel = new Label(String.format("$%.2f", itemTotal));
      priceLabel.getStyleClass().add("item-price");
      priceLabel.setMaxWidth(Double.MAX_VALUE);
      HBox.setHgrow(priceLabel, Priority.ALWAYS);
      priceLabel.setStyle("-fx-alignment: CENTER-RIGHT;");

      HBox itemRow = new HBox(nameLabel, priceLabel);
      itemRow.setSpacing(10);
      itemRow.setStyle("-fx-padding: 5 0 5 0; -fx-alignment: center-left; -fx-pref-width: 100%;");
      HBox.setHgrow(priceLabel, Priority.ALWAYS);
      itemRow.setFillHeight(true);

      itemsContainer.getChildren().add(itemRow);
    }
    orderIdLabel.setText("Order Number: " + order.getId());
    totalLabel.setText(String.format("Total: $%.2f", total));
    couponsLabel.setText(String.format("Coupons: %s", order.getDiscount().getCode()));
    thankYouLabel.setText("Thank you for dining with us!");
    restaurantNameLabel.setText("Restaurant Name: Gourmet Bistro");
    addressLabel.setText("Address: 123 Food St, Tasty Town");
    contactLabel.setText("Contact: (123) 456-7890");
  }

  public void goToHomePage() {
    sceneRouter.goToHomePage();
  }
}
