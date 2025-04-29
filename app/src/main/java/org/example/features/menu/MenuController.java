package org.example.features.menu;

import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.example.database.CrudRepository;
import org.example.features.order.OrderService;
import org.example.features.product.Product;
import org.example.shared.SceneRouter;

/** The type Menu controller. */
public class MenuController {

  private final MenuModel model;
  private final CrudRepository<Product, Integer> productRepository;
  private final SceneRouter sceneRouter;
  private final OrderService orderService;
  @FXML private VBox menuList;

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
    for (Product product : getMenuItems()) {
      Button addButton = new Button("Add to Order");

      Label name = new Label(product.getName());
      Label price = new Label(String.format("$%.2f", product.getPrice()));

      VBox productCard = new VBox(name, price, addButton);
      productCard.setSpacing(5);
      productCard.setStyle("-fx-padding: 10; -fx-border-color: #ccc; -fx-border-radius: 5;");

      // addButton.setOnAction(event -> orderService.addItem(product));
      addButton.setOnAction(event -> sceneRouter.goToProductDetailsPage(product));
      menuList.getChildren().add(productCard);
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
