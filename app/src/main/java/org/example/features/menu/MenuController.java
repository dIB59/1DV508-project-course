package org.example.features.menu;

import java.util.List;
import java.util.stream.Collectors;
import javafx.application.Platform;
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
import org.example.AppContext;
import org.example.SoundUtil;
import org.example.features.product.Product;
import org.example.features.product.ProductRepository;
import org.example.features.product.Tag;
import org.example.features.translation.Language;
import org.example.features.translation.TranslationService;
import org.example.shared.SceneRouter;

public class MenuController {

  private final ProductRepository productRepository;
  private final SceneRouter sceneRouter;
  private final TranslationService translationService;

  private List<Product> allProducts; // cache loaded products

  @FXML private AnchorPane menuView;
  @FXML private GridPane menuGrid;
  @FXML private VBox tagButtonContainer;

  public MenuController(
      ProductRepository productRepository,
      SceneRouter sceneRouter,
      TranslationService translationService) {
    this.productRepository = productRepository;
    this.sceneRouter = sceneRouter;
    this.translationService = translationService;
  }

  public void initialize() {
    try {
      allProducts = productRepository.findAll();
    } catch (Exception e) {
      System.err.println("Error loading menu items: " + e.getMessage());
      allProducts = List.of(); // fallback
    }

    populateTagButtons();
    displayAndTranslate(allProducts);

    Platform.runLater(
        () -> {
          if (menuView.getScene() != null
              && AppContext.getInstance().getLanguage() != Language.ENGLISH) {
            translationService.translate(menuView.getScene().getRoot());
          }
        });
  }

  private void populateTagButtons() {
    Button allButton = new Button("All");
    styleTagButton(allButton, true);
    allButton.setOnAction(e -> displayAndTranslate(allProducts));
    tagButtonContainer.getChildren().add(allButton);

    List<Tag> tags = productRepository.findAllTags();
    for (Tag tag : tags) {
      Button tagButton = new Button(tag.getName());
      styleTagButton(tagButton, false);
      tagButton.setOnAction(
          e -> {
            List<Product> filtered = filterProductsByTag(tag.getName());
            displayAndTranslate(filtered);
          });
      tagButtonContainer.getChildren().add(tagButton);
    }
  }

  private List<Product> filterProductsByTag(String tagName) {
    return allProducts.stream()
        .filter(p -> p.getTags().stream().anyMatch(t -> t.getName().equals(tagName)))
        .collect(Collectors.toList());
  }

  private void styleTagButton(Button button, boolean isPrimary) {
    String baseStyle =
        """
            -fx-padding: 10 20;
            -fx-font-weight: bold;
            -fx-background-radius: 20;
            -fx-cursor: hand;
        """;
    if (isPrimary) {
      button.setStyle(baseStyle + "-fx-background-color: -fx-color-muted; -fx-text-fill: -fx-color-background;");
    } else {
      button.setStyle(baseStyle + "-fx-background-color: -fx-color-border; -fx-text-fill: -fx-text-primary;");
    }
    button.setMaxWidth(Double.MAX_VALUE);
  }

  private void displayAndTranslate(List<Product> products) {
    displayProductCards(products);
    if (AppContext.getInstance().getLanguage() != Language.ENGLISH) {
      translationService.translate(menuView);
    }
  }

  private void displayProductCards(List<Product> products) {
    menuGrid.getChildren().clear();
    menuGrid.setHgap(15.0);
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
    imageView.setPreserveRatio(false);
    imageView.setSmooth(true);
    imageView.setCache(true);
    imageView.setFitWidth(400);
    imageView.setFitHeight(350);

    Label name = new Label(product.getName());
    name.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: -fx-text-primary;");
    name.setWrapText(true);
    name.setPadding(new Insets(0, 0, 0, 10));
    HBox.setHgrow(name, Priority.ALWAYS);

    Label price = new Label(String.format("SEK%.2f", product.getPrice()));
    price.getProperties().put(TranslationService.DO_NOT_TRANSLATE, true);

    price.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: -fx-color-muted;");

    Label description = new Label(product.getDescription());
    description.setStyle(
    """
    -fx-font-size: 14px;
    -fx-text-fill: -fx-color-border;
    """);

    description.setWrapText(true);
    description.setMaxWidth(280);

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
    -fx-background-color: -fx-color-background;
    -fx-padding: 20;
    -fx-border-radius: 10;
    -fx-background-radius: 10;
    -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 10, 0, 0, 0);
    """);


    card.setOnMouseClicked(
        e -> {
          sceneRouter.goToProductDetailsPage(product);
          SoundUtil.playClick();
        });

    if (product.getSpecialLabel() != null && !product.getSpecialLabel().isEmpty()) {
      Label special = new Label(product.getSpecialLabel());
      special.setStyle("""
        -fx-background-color: -fx-color-muted;
        -fx-text-fill: -fx-color-background;
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

  public void goToHelpPage(){sceneRouter.goToHelpView();}



  public List<Product> getMenuItems() {
    return allProducts;
  }
}
