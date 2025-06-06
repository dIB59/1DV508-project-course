package org.example.features.receipt;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Duration;

import org.example.features.dashboard.RestaurantSettings;
import org.example.features.dashboard.RestaurantSettingsRepository;
import org.example.features.ingredients.Ingredient;
import org.example.features.order.Order;
import org.example.features.order.ProductQuantity;
import org.example.features.product.Product;
import org.example.features.translation.TranslationService;
import org.example.members.MemberRepository;
import org.example.shared.SceneRouter;

public class ReceiptController {

  private final Order order;
  private final SceneRouter sceneRouter;
  private final MemberRepository memberRepository;
  private final RestaurantSettingsRepository repo;
  @FXML public Label orderIdLabel;
  public Label memberLabel;
  private PauseTransition autoRedirectPause;
  @FXML private VBox itemsContainer;
  @FXML private ScrollPane scrollPane;
  @FXML
  private Label titleLabel,
      totalLabel,
      couponsLabel,
      thankYouLabel,
      restaurantNameLabel,
      addressLabel,
      contactLabel,
      EatinEatoutlabel,
      pointsLabel;

  public ReceiptController(
      Order order, SceneRouter sceneRouter, MemberRepository memberRepository, RestaurantSettingsRepository repo) {
    this.order = order;
    this.sceneRouter = sceneRouter;
    this.memberRepository = memberRepository;
    this.repo = repo;
  }

  @FXML
  public void initialize() {

    List<ProductQuantity> productQuantities = order.getProductQuantity();

    for (ProductQuantity pq : productQuantities) {
      Product product = pq.getCustomizedProduct().getProduct();
      int quantity = pq.getQuantity();
      double itemTotal = pq.getCustomizedProduct().getTotalPrice() * quantity;

      // Left: Product name with quantity
      Label nameLabel = new Label(product.getName() + " x" + quantity);
      nameLabel.getStyleClass().add("item-name");
      VBox ingredientdiff = new VBox();
      ingredientdiff.setSpacing(3);
      Map<Ingredient, Integer> ingredients = pq.getCustomizedProduct().getIngredientquanities();
      Map<Ingredient, Integer> defaultIngs = product.getIngredients();

      for (Ingredient ingredient : defaultIngs.keySet()) {
        int ingQuantity = ingredients.getOrDefault(ingredient, 0);
        int defaultQty = defaultIngs.get(ingredient);

        if (ingQuantity != defaultQty) {
          String ingLabel;
          if (ingQuantity > defaultQty) {
            ingLabel = "+" + (ingQuantity - defaultQty) + " " + ingredient.getName();
          } else {
            ingLabel = "-" + (defaultQty - ingQuantity) + " " + ingredient.getName();
          }

          Label IngredientLabel = new Label(ingLabel);
          IngredientLabel.setFont(Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 12));
          ingredientdiff.getChildren().add(IngredientLabel);
        }
      }

      VBox nameAndIngredientsBox = new VBox(nameLabel, ingredientdiff);
      nameAndIngredientsBox.setSpacing(5);

      // Right: Price
      Label priceLabel = new Label(String.format("SEK%.2f", itemTotal));
      priceLabel.getProperties().put(TranslationService.DO_NOT_TRANSLATE, true);

      priceLabel.getStyleClass().add("item-price");
      priceLabel.setMaxWidth(Double.MAX_VALUE);
      HBox.setHgrow(priceLabel, Priority.ALWAYS);
      priceLabel.setStyle("-fx-alignment: CENTER-RIGHT;");

      HBox itemRow = new HBox(nameAndIngredientsBox, priceLabel);
      itemRow.setSpacing(10);
      itemRow.setStyle("-fx-padding: 5 0 5 0; -fx-alignment: center-left; -fx-pref-width: 100%;");
      HBox.setHgrow(priceLabel, Priority.ALWAYS);
      itemRow.setFillHeight(true);

      itemsContainer.getChildren().add(itemRow);
    }

    if (order.isMember()) {
      try {
        int personalNumber = order.getMemberId().orElse(0);
        int pointsToAdd = (int) Math.floor(order.getPrice()) * 10;
        memberRepository.addPoints(personalNumber, pointsToAdd);
      } catch (SQLException e) {
        System.err.println("Failed to add points: " + e.getMessage());
      }
    }

    EatinEatoutlabel.setText("Order Type: " + order.getType().name());
    orderIdLabel.setText("Order Number: " + order.getId());
    totalLabel.setText(String.format("Total: SEK%.2f", order.getPrice()));
    totalLabel.getProperties().put(TranslationService.DO_NOT_TRANSLATE, true);

    couponsLabel.setText(
        String.format(
            "Coupons: %s",
            order
                .getDiscount()
                .map(discount -> discount.getCode() + " -SEK" + discount.getDiscount())
                .orElse("No Coupons")));
    thankYouLabel.setText("Thank you for dining with us!");

    RestaurantSettings setting = repo.loadSettings();

    restaurantNameLabel.setText("name: " + setting.getName());
    addressLabel.setText("Address: " + setting.getAddress());
    contactLabel.setText("Contact: " + setting.getContact());
    memberLabel.setText("Member: " + order.getMemberId().map(String::valueOf).orElse("No Member"));
    pointsLabel.setText(
        "Points added: "
            + (int) Math.floor(order.getPrice()) * 10
            + " MemberID: "
            + order.getMemberId().orElse(0));
    startAutoRedirect();
  }

  public void goToHomePage() {
    if (autoRedirectPause != null) {
      autoRedirectPause.stop(); // Stop the timer if still running
    }
    sceneRouter.goToHomePage();
  }

  private void startAutoRedirect() {
    autoRedirectPause = new PauseTransition(Duration.seconds(7));
    autoRedirectPause.setOnFinished(e -> goToHomePage());
    autoRedirectPause.play();
  }
}
