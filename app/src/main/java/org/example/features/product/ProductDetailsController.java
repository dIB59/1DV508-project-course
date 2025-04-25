package org.example.features.product;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import org.example.features.order.OrderService;
import org.example.shared.SceneRouter;

public class ProductDetailsController {
  private final OrderService orderService;
  private final SceneRouter sceneRouter;
  private Product product;
  @FXML private Label productName;

  @FXML private Label productPrice;

  @FXML private Label productDescription;

  @FXML private Spinner<Integer> quantitySpinner;

  @FXML private Button addToOrderButton;

  public ProductDetailsController(OrderService orderService, SceneRouter sceneRouter) {
    this.orderService = orderService;
    this.sceneRouter = sceneRouter;
  }

  public void setProduct(Product product) {
    this.product = product;

    if (productName != null && productPrice != null && quantitySpinner != null) {
      // Update product details
      productName.setText(product.getName());
      productPrice.setText(String.format("$%.2f", product.getPrice()));
      System.err.println(product.getDescription());
      productDescription.setText(product.getDescription());

      // Set a SpinnerValueFactory to manage the Spinner's value
      SpinnerValueFactory<Integer> valueFactory =
          new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1); // Min:
      // 1,
      // Max:
      // 100,
      // Initial:
      // 1
      quantitySpinner.setValueFactory(valueFactory);

      // Ensure the Spinner is editable
      quantitySpinner.setEditable(true);

      // Add listener to validate input
      quantitySpinner
          .getEditor()
          .textProperty()
          .addListener(
              (observable, oldValue, newValue) -> {
                try {
                  int value = Integer.parseInt(newValue);
                  if (value < 1 || value > 100) {
                    quantitySpinner.getEditor().setText(oldValue); // Revert to old value if invalid
                  }
                } catch (NumberFormatException e) {
                  quantitySpinner
                      .getEditor()
                      .setText(oldValue); // Revert to old value if non-numeric
                }
              });
    } else {
      System.err.println("FXML fields are not initialized. Check FXML fx:id or initialization.");
    }
  }

  @FXML
  public void addToOrder() {
    Integer quantity = quantitySpinner.getValue(); // Get the current value from the Spinner
    if (quantity == null) {
      System.err.println("Spinner value is null. Ensure SpinnerValueFactory is set.");
      return;
    }

    for (int i = 0; i < quantity; i++) {
      orderService.addItem(product);
    }

    sceneRouter.goToMenuPage(); // reroute back to menu page once done
  }
}
