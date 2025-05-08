package org.example.features.product;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import org.example.features.order.OrderService;
import org.example.shared.SceneRouter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.SQLException;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ProductDetailsController {
  private final OrderService orderService;
  private final SceneRouter sceneRouter;
  private Product product;
  private final ProductRepository productRepository;

  private final Map<Product, Spinner<Integer>> sideSpinnerMap = new HashMap<>();


  @FXML private Label productName;

  @FXML private Label productPrice;

  @FXML private Label productDescription;

  @FXML private Spinner<Integer> quantitySpinner = new Spinner<>();

  @FXML private Button addToOrderButton;

  public ProductDetailsController(OrderService orderService, SceneRouter sceneRouter,ProductRepository productRepository) {
    this.orderService = orderService;
    this.sceneRouter = sceneRouter;
    this.productRepository = productRepository;
  }

  public void setProduct(Product product) {
    System.out.println("FUNCTION CALLED");
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
  @FXML private VBox sidesContainer;

  public void displaySides() {
    try {
      List<Product> sides = productRepository.findAll();
        if (sidesContainer != null) {
          sidesContainer.getChildren().clear();
          for (Product p : sides) {
            if (p.getisASide()) { // Use the correct getter
              Label sideLabel = new Label(p.getName() + " ($" + String.format("%.2f", p.getPrice()) + ")");
              Spinner<Integer> sideSpinner = new Spinner<>(1, 10, 1);
              sideSpinner.setPrefWidth(80);

              sideSpinnerMap.put(p, sideSpinner);

              HBox sideBox = new HBox(10, sideLabel, sideSpinner);
              sidesContainer.getChildren().add(sideBox);
            }
          }
        } else {
          System.err.println("sidesContainer is not initialized. Check FXML fx:id or initialization.");
        }
    } catch (SQLException e) {
      System.err.println("Error fetching sides: " + e.getMessage());
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
    //Add sides to the order based on spinner values
    for (Map.Entry<Product, Spinner<Integer>> entry : sideSpinnerMap.entrySet()) {
      Product side = entry.getKey();
      Integer sideQuantity = entry.getValue().getValue();
  
      for (int i = 0; i < sideQuantity; i++) {
        orderService.addItem(side);
      }
    }
    sceneRouter.goToMenuPage(); // reroute back to menu page once done
  }

}
