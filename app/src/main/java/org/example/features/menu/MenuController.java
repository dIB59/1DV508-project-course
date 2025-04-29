package org.example.features.menu;

import java.util.List;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.example.database.CrudRepository;
import org.example.features.order.OrderService;
import org.example.features.product.Product;
import org.example.shared.SceneRouter;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

/** The type Menu controller. */
public class MenuController {

  private final MenuModel model;
  private final CrudRepository<Product, Integer> productRepository;
  private final SceneRouter sceneRouter;
  private final OrderService orderService;
  @FXML private GridPane menuGrid;

  /**
   * Instantiates a new Menu controller.
   *
   * @param model the model
   * @param productRepository the product repository
   * @param sceneRouter the scene router
   * @param orderService the order service
   */
  public MenuController(
      MenuModel model,
      CrudRepository<Product, Integer> productRepository,
      SceneRouter sceneRouter,
      OrderService orderService) {
    this.model = model;
    this.productRepository = productRepository;
    this.sceneRouter = sceneRouter;
    this.orderService = orderService;
  }

  /** Initialize. */
  public void initialize() {

    int columns = 3;
    int row = 0;
    int col = 0;
    for (Product product : getMenuItems()) {
      Button addButton = new Button("Add to Order");
      addButton.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-background-radius: 5;");

      Label name = new Label(product.getName());
      name.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
      Label price = new Label(String.format("$%.2f", product.price()));
      Label specialLabel = new Label(product.getSpecialLabel());
      if (product.getSpecialLabel() != null && !product.getSpecialLabel().isEmpty()) {
        specialLabel.setStyle("""
                -fx-background-color: #e74c3c;
                -fx-text-fill: white;
                -fx-padding: 5 10;
                -fx-background-radius: 5;
                -fx-font-size: 14px;
                -fx-font-weight: bold;
            """);
      }

      VBox productInfo = new VBox(10, name ,price, addButton);
      productInfo.setStyle("-fx-alignment: center;");
      StackPane productCard = new StackPane();
      productCard.setStyle("""
            -fx-background-color: white;
            -fx-padding: 20;
            -fx-border-radius: 10;
            -fx-background-radius: 10;
            -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 10, 0, 0, 0);
        """);

      productCard.getChildren().addAll(productInfo);
      if (product.getSpecialLabel() != null && !product.getSpecialLabel().isEmpty()) {
        StackPane.setAlignment(specialLabel, Pos.TOP_RIGHT);
        productCard.getChildren().add(specialLabel);
      }

      addButton.setOnAction(event -> sceneRouter.goToProductDetailsPage(product));

      menuGrid.add(productCard, col, row);

      col++;
      if (col >= columns) {
        col = 0;
        row++;
      }

    }
  }

  /** Go to checkout page. */
  public void goToCheckoutPage() {
    sceneRouter.goToCheckoutPage();
  }

  /** Go to home page. */
  public void goToHomePage() {
    sceneRouter.goToHomePage();
  }

  /**
   * Gets menu items.
   *
   * @return the menu items
   */
  public List<Product> getMenuItems() {
    try {
      return productRepository.findAll();
    } catch (Exception e) {
      System.err.println("Error loading menu items: " + e.getMessage());
      return List.of();
    }
  }
}
