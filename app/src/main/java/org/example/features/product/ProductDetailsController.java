package org.example.features.product;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.example.features.ingredients.Ingredient;
import org.example.features.order.OrderService;
import org.example.features.translation.TranslationService;
import org.example.shared.SceneRouter;

public class ProductDetailsController {
  private final OrderService orderService;
  private final SceneRouter sceneRouter;
  private final ProductRepository productRepository;
  private final Map<Product, Spinner<Integer>> sideSpinnerMap = new HashMap<>();
  private final Map<Ingredient, Spinner<Integer>> ingredientSpinnerMap = new HashMap<>();
  private Product product;
  @FXML private Label productName;

  @FXML private Label productPrice;

  @FXML private Label productDescription;

  @FXML private Spinner<Integer> quantitySpinner = new Spinner<>();

  @FXML private Button addToOrderButton;

  @FXML private VBox ingredientsContainer;
  @FXML private VBox sidesContainer;

  @FXML private Label totalPriceLabel;

  @FXML private Button goBackButton;

  public ProductDetailsController(
      OrderService orderService, SceneRouter sceneRouter, ProductRepository productRepository) {
    this.orderService = orderService;
    this.sceneRouter = sceneRouter;
    this.productRepository = productRepository;
  }

  public void setProduct(Product product) {
    this.product = product;
    if (productName != null && productPrice != null && quantitySpinner != null) {
      // Update product details
      productName.setText(product.getName());
      productPrice.setText(String.format("SEK%.2f", product.getPrice()));
      productPrice.getProperties().put(TranslationService.DO_NOT_TRANSLATE, true);
      productDescription.setText(product.getDescription());

      // Set a SpinnerValueFactory to manage the Spinner's value
      SpinnerValueFactory<Integer> valueFactory =
          new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1);
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

  public void displayIngredients() {
    if (ingredientsContainer != null) {
      ingredientsContainer.getChildren().clear();
      ingredientSpinnerMap.clear();

      Map<Ingredient, Integer> ingredients = product.getIngredients();

      for (Ingredient ing : ingredients.keySet()) {
        Label nameLabel = new Label(ing.getName());
        nameLabel.setStyle("-fx-text-fill: -fx-text-primary;");
        nameLabel.setMaxWidth(Double.MAX_VALUE); // Allow ellipsis
        nameLabel.setEllipsisString("...");

        Label priceLabel = new Label("(SEK " + String.format("%.2f", ing.getPrice()) + ")");
        priceLabel.setStyle("-fx-text-fill: -fx-text-secondary;");
        priceLabel.getProperties().put(TranslationService.DO_NOT_TRANSLATE, true);

        HBox namePriceBox = new HBox(5, nameLabel, priceLabel);
        namePriceBox.setStyle("-fx-alignment: center-left;");
        HBox.setHgrow(nameLabel, Priority.ALWAYS);
        HBox.setHgrow(priceLabel, Priority.NEVER);

        Spinner<Integer> ingSpinner = new Spinner<>(0, 10, ingredients.get(ing));
        ingSpinner.setPrefWidth(80);
        ingSpinner.setStyle(
            "-fx-background-color: -fx-color-foreground;" +
                "-fx-text-fill: -fx-text-primary;"
        );
        ingredientSpinnerMap.put(ing, ingSpinner);

        HBox ingBox = new HBox(20, namePriceBox, ingSpinner); // increased spacing
        ingBox.setStyle("-fx-background-color: -fx-color-background;");
        HBox.setHgrow(namePriceBox, Priority.ALWAYS);

        ingredientsContainer.getChildren().add(ingBox);
      }
    }
  }


  public void displaySides() {
    try {
      List<Product> sides = productRepository.findAll();
      if (sidesContainer != null) {
        sidesContainer.getChildren().clear();
        for (Product p : sides) {
          if (p.getisASide()) {
            Label nameLabel = new Label(p.getName());
            nameLabel.setStyle("-fx-text-fill: -fx-text-primary;");
            nameLabel.setMaxWidth(Double.MAX_VALUE);
            nameLabel.setEllipsisString("...");

            Label priceLabel = new Label(" (SEK" + String.format("%.2f", p.getPrice()) + ")");
            priceLabel.setStyle("-fx-text-fill: -fx-text-secondary;");
            priceLabel.getProperties().put(TranslationService.DO_NOT_TRANSLATE, true);

            HBox namePriceBox = new HBox(5, nameLabel, priceLabel);
            namePriceBox.setStyle("-fx-alignment: center-left;");
            HBox.setHgrow(nameLabel, Priority.ALWAYS);
            HBox.setHgrow(priceLabel, Priority.NEVER);

            Spinner<Integer> sideSpinner = new Spinner<>(0, 10, 0);
            sideSpinner.setPrefWidth(80);

            sideSpinnerMap.put(p, sideSpinner);

            HBox sideBox = new HBox(20, namePriceBox, sideSpinner); // increased spacing
            HBox.setHgrow(namePriceBox, Priority.ALWAYS);

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

    Map<Ingredient, Integer> selectedIngredients = new HashMap<>();

    // adding ingredients and products
    for (Map.Entry<Ingredient, Spinner<Integer>> entry : ingredientSpinnerMap.entrySet()) {
      Ingredient ingredient = entry.getKey();
      Integer ingQuantity = entry.getValue().getValue();

      if (ingQuantity != null && ingQuantity > 0) {
        selectedIngredients.put(ingredient, ingQuantity);
      }
    }

    for (int i = 0; i < quantity; i++) {
      if (!selectedIngredients.isEmpty()) {
        orderService.addItem(product, selectedIngredients);
      } else {
        orderService.addItem(product, new HashMap<>());
      }
    }

    // handeling sides
    for (Map.Entry<Product, Spinner<Integer>> entry : sideSpinnerMap.entrySet()) {
      Product side = entry.getKey();
      Integer sideQuantity = entry.getValue().getValue();

      if (sideQuantity != null && sideQuantity > 0) {
        for (int i = 0; i < sideQuantity; i++) {
          orderService.addItem(side, new HashMap<>());
        }
      }
    }
    sceneRouter.goToMenuPage();
  }

  @FXML
  private void goBack() {
    sceneRouter.goToMenuPage(); // or goToHomePage() if you prefer
  }
}
