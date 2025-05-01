package org.example.features.menu;

import java.sql.SQLException;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.example.database.CrudRepository;
import org.example.features.order.OrderService;
import org.example.features.product.Product;
import org.example.features.product.ProductRepository;
import org.example.features.product.Tag;
import org.example.shared.SceneRouter;


/** The type Menu controller. */
public class MenuController {

  private final MenuModel model;
  private final ProductRepository productRepository;
  private final SceneRouter sceneRouter;
  private final OrderService orderService;
  @FXML private VBox menuList;
  @FXML private VBox tagButtonContainer;

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
      ProductRepository productRepository,
      SceneRouter sceneRouter,
      OrderService orderService) {
    this.model = model;
    this.productRepository = productRepository;
    this.sceneRouter = sceneRouter;
    this.orderService = orderService;
  }

  /** Initialize. */
  public void initialize() {
    populateTagButtons();
    displayProductCards(getMenuItems()); // Default to showing all items
  }

  private void populateTagButtons() {
    Button allButton = new Button("All");
    allButton.setMaxWidth(Double.MAX_VALUE);
    allButton.setStyle("-fx-background-color: #ddd; -fx-padding: 10; -fx-font-weight: bold;");

    allButton.setOnAction(e -> displayProductCards(getMenuItems()));
    tagButtonContainer.getChildren().add(allButton);
    try {
      List<Tag> tags = productRepository.findAllTags(); // You'll need to implement this
      for (Tag tag : tags) {
        Button tagButton = getTagButton(tag);
        tagButtonContainer.getChildren().add(tagButton);
      }
    } catch (SQLException e) {
      System.err.println("Error fetching tags: " + e.getMessage());
    }
  }

  private Button getTagButton(Tag tag) {
    Button tagButton = new Button(tag.getName());
    tagButton.setMaxWidth(Double.MAX_VALUE);
    tagButton.setStyle("-fx-background-color: #ddd; -fx-padding: 10;");

    tagButton.setOnAction(e -> {
      try {
        List<Product> filteredProducts = productRepository.findProductsByTagName(tag.getName());
        displayProductCards(filteredProducts);
      } catch (SQLException ex) {
        System.err.println("Error loading products for tag " + tag + ": " + ex.getMessage());
      }
    });
    return tagButton;
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
  private void displayProductCards(List<Product> products) {
    menuList.getChildren().clear(); // remove old cards

    for (Product product : products) {
      Button addButton = new Button("Add to Order");

      Label name = new Label(product.getName());
      Label price = new Label(String.format("$%.2f", product.getPrice()));

      VBox productCard = new VBox(name, price, addButton);
      productCard.setSpacing(5);
      productCard.setStyle("-fx-padding: 10; -fx-border-color: #ccc; -fx-border-radius: 5;");

      addButton.setOnAction(event -> sceneRouter.goToProductDetailsPage(product));

      menuList.getChildren().add(productCard);
    }
  }
}
