package org.example.features.dashboard;

import java.sql.SQLException;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.database.CrudRepository;
import org.example.features.product.Product;
import org.example.shared.SceneRouter;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;


public class DashboardController {

  private final DashboardModel dashboardModel;
  private final SceneRouter sceneRouter;
  private final CrudRepository<Product> repository;

  @FXML private VBox productList; // The VBox inside the ScrollPane

  public DashboardController(DashboardModel dashboardModel,
                             SceneRouter sceneRouter,
                             CrudRepository<Product> repository) {
    this.dashboardModel = dashboardModel;
    this.sceneRouter = sceneRouter;
    this.repository = repository;
  }

  public void initialize() {
    loadProducts();
  }

  public void goToHomePage() {
    sceneRouter.goToHomePage();
  }

  private void loadProducts() {

    List<Product> products;
    try {
      products = repository.findAll();
    } catch (Exception e) {
      System.err.println("Error loading products: " + e.getMessage());
      return;
    }
    productList.getChildren().clear();

    for (Product product : products) {
      HBox productCard = createProductCard(product);
      productList.getChildren().add(productCard);
    }

  }

  private HBox createProductCard(Product product) {
    HBox card = new HBox(20);
    card.setAlignment(Pos.CENTER_LEFT);
    card.setPadding(new Insets(10));
    card.setStyle("-fx-background-color: #f9f9f9; -fx-background-radius: 12; -fx-border-color: #cccccc; -fx-border-radius: 12;");
    card.setPrefHeight(120);

    // Product Image
    ImageView imageView = new ImageView();
    try {
      if (product.imageUrl() != null && !product.imageUrl().isBlank()) {
        imageView.setImage(new Image(product.imageUrl(), true));
      }
    } catch (Exception e) {
      System.out.println("Could not load product image: " + e.getMessage());
    }
    imageView.setFitHeight(80);
    imageView.setFitWidth(80);
    imageView.setPreserveRatio(true);
    imageView.setSmooth(true);

    // Text Info (name + price)
    VBox infoBox = new VBox(5);
    infoBox.setAlignment(Pos.CENTER_LEFT);

    Label nameLabel = new Label(product.name());
    nameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: black;");

    Label priceLabel = new Label(String.format("$%.2f", product.price()));
    priceLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #777777;");

    infoBox.getChildren().addAll(nameLabel, priceLabel);

    // Edit Button
    Button editButton = new Button("Edit");
    editButton.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-background-radius: 5; -fx-cursor: hand;");
    editButton.setOnAction(e -> editProduct(product));

    HBox spacer = new HBox(); // Pushes the button to the right
    spacer.setMinWidth(Region.USE_COMPUTED_SIZE);
    HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

    card.getChildren().addAll(imageView, infoBox, spacer, editButton);

    return card;
  }


  private void editProduct(Product product) {
    Stage dialog = new Stage();
    dialog.initModality(Modality.APPLICATION_MODAL);
    dialog.setTitle("Edit Product");

    VBox vbox = new VBox(10);
    vbox.setPadding(new Insets(20));

    TextField nameField = new TextField(product.name());
    TextField descriptionField = new TextField(product.description());
    TextField priceField = new TextField(String.valueOf(product.price()));
    TextField imageUrlField = new TextField(product.imageUrl());

    Button saveButton = new Button("Save");
    saveButton.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-background-radius: 5;");

    saveButton.setOnAction(
        e -> {
          try {
            String newName = nameField.getText();
            String newDescription = descriptionField.getText();
            double newPrice = Double.parseDouble(priceField.getText());
            String newImageUrl = imageUrlField.getText();

            Product updatedProduct =
                new Product(product.id(), newName, newDescription, newPrice, newImageUrl);
            System.out.println(updatedProduct);
            repository.update(updatedProduct);

            loadProducts(); // refresh
            dialog.close();
          } catch (NumberFormatException ex) {
            showAlert("Invalid price format.");
          } catch (IllegalArgumentException ex) {
            showAlert(ex.getMessage());
          } catch (SQLException ex) {
            showAlert("Error updating product: " + ex.getMessage());
          }
        });

    vbox.getChildren().addAll(
        new Label("Name:"), nameField,
        new Label("Description:"), descriptionField,
        new Label("Price:"), priceField,
        new Label("Image URL:"), imageUrlField,
        saveButton
    );

    Scene scene = new Scene(vbox, 400, 400);
    dialog.setScene(scene);
    dialog.showAndWait();
  }


  private void showAlert(String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Error");
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }
}

