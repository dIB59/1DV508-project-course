package org.example.features.checkout;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.example.features.order.OrderService;
import org.example.features.order.ProductQuantity;
import org.example.shared.SceneRouter;

/**
 * The type Checkout controller.
 */
public class CheckoutController implements Initializable {

  private final OrderService orderService;
  private final SceneRouter router;
  @FXML
  private Label itemCountLabel;
  @FXML
  private Label totalPriceLabel;
  @FXML
  private ListView<String> itemListView;

  /**
   * Instantiates a new Checkout controller.
   *
   * @param orderService the order service
   * @param sceneRouter  the scene router
   */
  public CheckoutController(OrderService orderService, SceneRouter sceneRouter) {
    this.orderService = orderService;
    this.router = sceneRouter;
  }

  /**
   * Go to home page.
   */
  public void goToHomePage() {
    orderService.saveOrder();
    orderService.clearItems();
    router.goToHomePage();
  }

  /**
   * Gets items.
   *
   * @return the items
   */
  public List<ProductQuantity> getItems() {
    return orderService.getItems();
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    int itemCount = orderService.getItems().size();
    itemCountLabel.setText("Items in cart: " + itemCount);

    // Populate ListView
    ObservableList<String> items = FXCollections.observableArrayList();
    for (ProductQuantity item : orderService.getItems()) {
      items.add(item.getProduct().name() + " - $" + item.getProduct().getPrice() + " x "
          + item.getQuantity());
    }
    itemListView.setItems(items);
    double totalPrice = orderService.getItems().stream()
        .mapToDouble(ProductQuantity::getPrice)
        .sum();
    totalPriceLabel.setText("Total Price: $" + String.format("%.2f", totalPrice));
  }
}
