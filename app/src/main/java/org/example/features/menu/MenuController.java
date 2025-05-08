package org.example.features.menu;

import java.sql.SQLException;
import java.util.List;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;

import org.example.features.order.OrderService;
import org.example.features.product.Product;
import org.example.features.product.ProductRepository;
import org.example.features.product.Tag;
import org.example.shared.SceneRouter;

public class MenuController {

  private final MenuModel model;
  private final ProductRepository productRepository;
  private final SceneRouter sceneRouter;
  private final OrderService orderService;

  @FXML private GridPane menuGrid;
  @FXML private HBox tagButtonContainer;

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

  public void initialize() {
    populateTagButtons();
    displayProductCards(getMenuItems());
  }

  private void populateTagButtons() {
    Button allButton = new Button("All");
    styleTagButton(allButton, true);
    allButton.setOnAction(e -> displayProductCards(getMenuItems()));
    tagButtonContainer.getChildren().add(allButton);

    try {
      List<Tag> tags = productRepository.findAllTags();
      for (Tag tag : tags) {
        Button tagButton = new Button(tag.getName());
        styleTagButton(tagButton, false);
        tagButton.setOnAction(e -> {
          try {
            List<Product> filteredProducts = productRepository.findProductsByTagName(tag.getName());
            displayProductCards(filteredProducts);
            System.out.println("Filtered products for tag " + tag.getName() + ": " + filteredProducts);
          } catch (SQLException ex) {
            System.err.println("Error loading products for tag " + tag.getName() + ": " + ex.getMessage());
          }
        });
        tagButtonContainer.getChildren().add(tagButton);
      }
    } catch (SQLException e) {
      System.err.println("Error fetching tags: " + e.getMessage());
    }
  }

  /** Go to memberlogin page. */
  public void goToMemberLogin() {
    sceneRouter.goToMemberLoginPage();
  }

  private void styleTagButton(Button button, boolean isPrimary) {
    String baseStyle = """
            -fx-padding: 10 20;
            -fx-font-weight: bold;
            -fx-background-radius: 20;
            -fx-cursor: hand;
        """;
    if (isPrimary) {
      button.setStyle(baseStyle + "-fx-background-color: #2c3e50; -fx-text-fill: white;");
    } else {
      button.setStyle(baseStyle + "-fx-background-color: #bdc3c7; -fx-text-fill: #2c3e50;");
    }
    button.setMaxWidth(Double.MAX_VALUE);
  }

  private void displayProductCards(List<Product> products) {
    menuGrid.getChildren().clear();
    menuGrid.setHgap(15.0); // Reduced from 30.0
    menuGrid.setVgap(15.0);
    menuGrid.setAlignment(Pos.CENTER);
    menuGrid.getRowConstraints().clear();
    menuGrid.getColumnConstraints().clear();

    int columns = 4;
    double columnWidth = 400;
    for (int i = 0; i < columns; i++) {
      ColumnConstraints column = new ColumnConstraints();
      column.setPrefWidth(columnWidth);
      column.setMinWidth(columnWidth);
      column.setMaxWidth(columnWidth);
      column.setHalignment(HPos.CENTER);
      menuGrid.getColumnConstraints().add(column);
  }

  int col = 0;
  int row = 0;

  for (Product product : products) {
      StackPane productCard = createProductCard(product);
      
      // Set exact size for the card
      productCard.setPrefWidth(400);
      productCard.setMaxWidth(400);
      productCard.setMinWidth(400);
      
      menuGrid.add(productCard, col, row);
      col++;
      if (col >= columns) {
          col = 0;
          row++;
      }
    }
  }

  private StackPane createProductCard(Product product) {

    ImageView imageView = new ImageView();
    try {
      if (product.getImageUrl() != null && !product.getImageUrl().isBlank()) {
        imageView.setImage(new Image(product.getImageUrl(), true));
      }
    } catch (Exception e) {
      System.out.println("Could not load product image: " + e.getMessage());
    }

    Rectangle clip = new Rectangle(400, 350);
    clip.setArcWidth(30);
    clip.setArcHeight(30);
    imageView.setClip(clip);

    imageView.setPreserveRatio(false); // Do not preserve aspect ratio, stretch to fit
    imageView.setSmooth(true);
    imageView.setCache(true);
    imageView.setFitWidth(400);
    imageView.setFitHeight(350);
    

    Label name = new Label(product.getName());
    name.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
    name.setWrapText(true);
    name.setPadding(new Insets(0, 0, 0, 10)); 
    HBox.setHgrow(name, Priority.ALWAYS);

    Label price = new Label(String.format("$%.2f", product.getPrice()));
    price.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #555555;");

    Label description = new Label(product.getDescription());
    description.setStyle("""
            -fx-font-size: 14px;
            -fx-text-fill: #666666;
            """);
    description.setWrapText(true);
    description.setMaxWidth(280);

    /*Button addButton = new Button("Add to Order");
    addButton.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-background-radius: 5;");
    addButton.setOnAction(e -> sceneRouter.goToProductDetailsPage(product)); */

    HBox textContainer = new HBox(10);
    textContainer.setAlignment(Pos.CENTER_LEFT);
    textContainer.setPadding(new Insets(8, 0, 0, 0));
    textContainer.getChildren().addAll(name, price);

    VBox productInfo = new VBox(8);
    productInfo.getChildren().addAll(imageView, textContainer, description);
    productInfo.setAlignment(Pos.TOP_CENTER);
    productInfo.setPadding(new Insets(0, 0, 10, 0));

    StackPane card = new StackPane(productInfo);
    card.setStyle("""
            -fx-background-color: white;
            -fx-padding: 20;
            -fx-border-radius: 10;
            -fx-background-radius: 10;
            -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 10, 0, 0, 0);
        """);

    card.setOnMouseClicked(e -> sceneRouter.goToProductDetailsPage(product));


    if (product.getSpecialLabel() != null && !product.getSpecialLabel().isEmpty()) {
      Label special = new Label(product.getSpecialLabel());
      special.setStyle("""
                -fx-background-color: #e74c3c;
                -fx-text-fill: white;
                -fx-padding: 5 10;
                -fx-background-radius: 5;
                -fx-font-size: 14px;
                -fx-font-weight: bold;
            """);
      StackPane.setAlignment(special, Pos.TOP_RIGHT);
      card.getChildren().add(special);
    }

    return card;
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
