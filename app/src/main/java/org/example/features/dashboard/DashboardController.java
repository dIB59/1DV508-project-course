package org.example.features.dashboard;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.features.product.Product;
import org.example.features.product.ProductRepository;
import org.example.features.product.Tag;
import org.example.shared.SceneRouter;

public class DashboardController {

  private final DashboardModel dashboardModel;
  private final SceneRouter sceneRouter;
  private final ProductRepository repository;
  @FXML private VBox productList; // The VBox inside the ScrollPane

  public DashboardController(
      DashboardModel dashboardModel, SceneRouter sceneRouter, ProductRepository repository) {
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

  public void goToCouponsPage() {
    sceneRouter.goToCouponsPage();
  }

  private void loadProducts() {

    List<Product> products;
    try {
      products = repository.findAll();
    } catch (SQLException e) {
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
    HBox card = new HBox();
    card.setAlignment(Pos.CENTER_LEFT);
    card.setPadding(new Insets(0));
    card.setSpacing(0);
    card.setPrefHeight(140); // Fixed height
    card.setStyle(
        "-fx-background-color: #f9f9f9;"
            + " -fx-background-radius: 12;"
            + " -fx-border-color: #cccccc;"
            + " -fx-border-radius: 0 20 20 0;");

    // Image setup
    ImageView imageView = new ImageView();
    try {
      if (product.getImageUrl() != null && !product.getImageUrl().isBlank()) {
        imageView.setImage(new Image(product.getImageUrl(), true));
      }
    } catch (Exception e) {
      System.out.println("Could not load product image: " + e.getMessage());
    }
    imageView.setPreserveRatio(false); // Do not preserve aspect ratio, stretch to fit
    imageView.setSmooth(true);
    imageView.setCache(true);
    imageView.setFitWidth(140);
    imageView.setFitHeight(140);
    // Removed background and border styles from imageView

    // Image Container
    StackPane imageContainer = new StackPane(imageView);
    imageContainer.setPrefSize(140, 140);
    imageContainer.setMaxHeight(140);
    imageContainer.setMaxWidth(140);

    // Info Section
    VBox infoBox = new VBox(5);
    infoBox.setAlignment(Pos.CENTER_LEFT);
    infoBox.setPadding(new Insets(10));

    Label nameLabel = new Label(product.getName());
    nameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: black;");

    Label priceLabel = new Label(String.format("$%.2f", product.getPrice()));
    priceLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #777777;");

    infoBox.getChildren().addAll(nameLabel, priceLabel);

    // Edit Button with Icon (FontAwesome or any icon font)
    Button editButton = new Button();
    Text editIcon = new Text("✎");
    editIcon.setFont(Font.font(24)); // Set icon size
    editIcon.setFill(Color.WHITE); // Set icon color
    editButton.setGraphic(editIcon);
    editButton.setStyle(
        "-fx-background-color: black; -fx-text-fill: white; -fx-background-radius: 0 10 10 0; -fx-cursor: hand;");
    editButton.setOnAction(e -> editProduct(product));

    // Stretch the button vertically and place at the bottom
    VBox.setVgrow(editButton, Priority.ALWAYS);
    editButton.setMaxHeight(Double.MAX_VALUE); // Allow it to stretch to max height

    HBox spacer = new HBox();
    HBox.setHgrow(spacer, Priority.ALWAYS);

    // Assemble
    card.getChildren().addAll(imageContainer, infoBox, spacer, editButton);

    return card;
  }

  private void editProduct(Product product) {
    Stage dialog = new Stage();
    dialog.initModality(Modality.APPLICATION_MODAL);
    dialog.setTitle("Edit Product");

    VBox vbox = new VBox(10);
    vbox.setPadding(new Insets(20));

    TextField nameField = new TextField(product.getName());
    TextField descriptionField = new TextField(product.getDescription());
    TextField priceField = new TextField(String.valueOf(product.getPrice()));
    TextField imageUrlField = new TextField(product.getImageUrl());

    // Fetch all tags
    List<Tag> allTags;
    try {
      allTags = repository.findAllTags();
    } catch (SQLException e) {
      showAlert("Error fetching tags: " + e.getMessage());
      return;
    }
    VBox tagsBox = new VBox(5);
    List<CheckBox> tagCheckboxes = new ArrayList<>();

    for (Tag tag : allTags) {
      CheckBox checkBox = new CheckBox(tag.getName());
      if (product.tagIds().contains(tag.getId())) {
        checkBox.setSelected(true);
      }
      tagCheckboxes.add(checkBox);
      tagsBox.getChildren().add(checkBox);
    }

    // HBox for Tags Label + Add Button
    HBox tagsLabelBox = new HBox(5);
    Label tagsLabel = new Label("Tags:");
    Button addTagButton = new Button("+");
    addTagButton.setStyle(
        "-fx-background-color: black; -fx-text-fill: white; -fx-background-radius: 5;");

    tagsLabelBox.getChildren().addAll(tagsLabel, addTagButton);

    addTagButton.setOnAction(
        e -> {
          TextInputDialog inputDialog = new TextInputDialog();
          inputDialog.setTitle("Add New Tag");
          inputDialog.setHeaderText(null);
          inputDialog.setContentText("Enter new tag name:");

          inputDialog
              .showAndWait()
              .ifPresent(
                  tagName -> {
                    if (!tagName.trim().isEmpty()) {
                      int newTagId;
                      try {
                        newTagId = repository.createTag(tagName.trim());
                      } catch (SQLException ex) {
                        showAlert("Error adding tag: " + ex.getMessage());
                        return;
                      }

                      Tag newTag = new Tag(newTagId, tagName.trim());
                      allTags.add(newTag);

                      CheckBox newCheckBox = new CheckBox(newTag.getName());
                      newCheckBox.setSelected(true);
                      tagCheckboxes.add(newCheckBox);
                      tagsBox.getChildren().add(newCheckBox);
                    }
                  });
        });

    Button saveButton = new Button("Save");
    saveButton.setStyle(
        "-fx-background-color: black; -fx-text-fill: white; -fx-background-radius: 5;");

    saveButton.setOnAction(
        e -> {
          try {
            String newName = nameField.getText();
            String newDescription = descriptionField.getText();
            double newPrice = Double.parseDouble(priceField.getText());
            String newImageUrl = imageUrlField.getText();

            List<Integer> selectedTagIds = new ArrayList<>();
            List<String> selectedTagNames = new ArrayList<>();
            for (int i = 0; i < tagCheckboxes.size(); i++) {
              CheckBox checkBox = tagCheckboxes.get(i);
              if (checkBox.isSelected()) {
                selectedTagIds.add(allTags.get(i).getId());
                selectedTagNames.add(allTags.get(i).getName());
              }
            }
            List<Tag> selectedTags = new ArrayList<>();
            for (int i = 0; i < selectedTagIds.size(); i++) {
              selectedTags.add(new Tag(selectedTagIds.get(i), selectedTagNames.get(i)));
            }

            Product updatedProduct =
                new Product(
                    product.getId(), newName, newDescription, newPrice, newImageUrl, selectedTags);

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

    vbox.getChildren()
        .addAll(
            new Label("Name:"),
            nameField,
            new Label("Description:"),
            descriptionField,
            new Label("Price:"),
            priceField,
            new Label("Image URL:"),
            imageUrlField,
            tagsLabelBox,
            tagsBox,
            saveButton);

    Scene scene = new Scene(vbox, 400, 600);
    dialog.setScene(scene);
    dialog.showAndWait();
  }

  private void showAlert(String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Error");
    alert.setHeaderText(null);

    System.err.println("Error: " + message);
    alert.setContentText(message);
    alert.showAndWait();
  }
}
