package org.example.features.menu;

import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.example.features.order.OrderService;
import org.example.features.product.Product;
import org.example.features.product.ProductRepository;
import org.example.shared.SceneRouter;

public class MenuController {

  private final MenuModel model;
  private final ProductRepository productRepository;
  private final SceneRouter sceneRouter;
  private final OrderService orderService;
  @FXML
  private VBox menuList;

  public MenuController(MenuModel model, ProductRepository productRepository,
                        SceneRouter sceneRouter, OrderService orderService) {
    this.model = model;
    this.productRepository = productRepository;
    this.sceneRouter = sceneRouter;
    this.orderService = orderService;
  }

  public void initialize() {
    for (Product product : getMenuItems()) {
      Button addButton = new Button("Add to Order");

      Label name = new Label(product.getName());
      Label price = new Label(String.format("$%.2f", product.price()));

      VBox productCard = new VBox(name, price, addButton);
      productCard.setSpacing(5);
      productCard.setStyle("-fx-padding: 10; -fx-border-color: #ccc; -fx-border-radius: 5;");

      addButton.setOnAction(event -> orderService.addItem(product));

      menuList.getChildren().add(productCard);
    }
  }

  public void goToCheckoutPage() {
    sceneRouter.goToCheckoutPage();
  }

  public void goToHomePage() {
    sceneRouter.goToHomePage();
  }

  public List<Product> getMenuItems() {
    try {
      return productRepository.findAll();
    } catch (Exception e) {
      System.err.println("Error loading menu items: " + e.getMessage());
      return List.of();
    }
  }
}
