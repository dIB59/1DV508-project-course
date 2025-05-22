package org.example.features.dashboard;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
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
import javafx.stage.FileChooser;
import java.io.File;
import javafx.scene.image.Image;
import java.io.ByteArrayInputStream;


public class DashboardController {

  private final SceneRouter sceneRouter;
  private final ProductRepository repository;
  @FXML private VBox productList; // The VBox inside the ScrollPane

  public DashboardController(SceneRouter sceneRouter, ProductRepository repository) {
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
  private boolean isImageFile(File file) {
    String fileName = file.getName().toLowerCase();
    return fileName.endsWith(".png") || fileName.endsWith(".jpg") ||
            fileName.endsWith(".jpeg") || fileName.endsWith(".gif");
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
    card.setPrefHeight(140);
    card.setStyle(
        "-fx-background-color: #f9f9f9;"
            + " -fx-background-radius: 12;"
            + " -fx-border-color: #cccccc;"
            + " -fx-border-radius: 0 20 20 0;");

    // Image setup
    ImageView imageView = new ImageView();
    try {
      Image image = product.getImage();
      if (image != null) {
        imageView.setImage(image);
      }
    } catch (IllegalArgumentException e) {
        System.err.println("Error loading image: " + e.getMessage());
        imageView.setImage(new Image("default_image.png")); // Placeholder image
        } catch (Exception e) {
        System.err.println("Error loading image: " + e.getMessage());
        imageView.setImage(new Image("default_image.png")); // Placeholder image
    }
    imageView.setPreserveRatio(false);
    imageView.setSmooth(true);
    imageView.setCache(true);
    imageView.setFitWidth(140);
    imageView.setFitHeight(140);

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

    HBox spacer = new HBox();
    HBox.setHgrow(spacer, Priority.ALWAYS);

    // Edit Button (top half)
    Button editButton = new Button();
    Text editIcon = new Text("✎");
    editIcon.setFont(Font.font(20));
    editIcon.setFill(Color.WHITE);
    editButton.setGraphic(editIcon);
    editButton.setStyle(
        "-fx-background-color: black; -fx-text-fill: white;"
            + " -fx-background-radius: 0 10 0 0;"
            + " -fx-cursor: hand;");
    editButton.setMinHeight(70);
    editButton.setMaxHeight(70);
    editButton.setMaxWidth(Double.MAX_VALUE);
    editButton.setOnAction(e -> editProduct(product));


    // Delete Button (bottom half) with bin icon
    Button deleteButton = new Button();
    Text binIcon = new Text("X");
    binIcon.setFont(Font.font(18));
    binIcon.setFill(Color.WHITE);
    deleteButton.setGraphic(binIcon);
    deleteButton.setStyle(
        "-fx-background-color: #6f1515; -fx-text-fill: white;"
            + " -fx-background-radius: 0 0 10 0;"
            + " -fx-cursor: hand;");
    deleteButton.setMinHeight(70);
    deleteButton.setMaxHeight(70);
    deleteButton.setMaxWidth(Double.MAX_VALUE);
    deleteButton.setOnAction(e -> {
      try {
        repository.delete(product.getId());
        loadProducts();
      } catch (SQLException ex) {
        showAlert("Error deleting product: " + ex.getMessage());
      }
    });

    // Combine edit and delete into a VBox
    VBox actionBox = new VBox();
    actionBox.setPrefHeight(140);
    actionBox.setPrefWidth(50); // or adjust as needed
    actionBox.getChildren().addAll(editButton, deleteButton);

    // Assemble full card
    card.getChildren().addAll(imageContainer, infoBox, spacer, actionBox);
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
    HBox imageBox = new HBox(5);
    TextField imageUrlField = new TextField(product.getImageUrl());
    Button browseButton = new Button("Browse");
    browseButton.setOnAction(e -> {
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Choose Image File");
      fileChooser.getExtensionFilters().addAll(
              new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
      );
      File selectedFile = fileChooser.showOpenDialog(dialog);
      if (selectedFile != null) {
        imageUrlField.setText(selectedFile.toURI().toString());
      }
    });
    imageBox.getChildren().addAll(imageUrlField, browseButton);

    TextField specialLabelField = new TextField(product.getSpecialLabel());

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
    addTagButton.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-background-radius: 5;");
    tagsLabelBox.getChildren().addAll(tagsLabel, addTagButton);

    addTagButton.setOnAction(e -> {
      TextInputDialog inputDialog = new TextInputDialog();
      inputDialog.setTitle("Add New Tag");
      inputDialog.setHeaderText(null);
      inputDialog.setContentText("Enter new tag name:");

      inputDialog.showAndWait().ifPresent(tagName -> {
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
    saveButton.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-background-radius: 5;");

    saveButton.setOnAction(e -> {
      try {
        String newName = nameField.getText();
        String newDescription = descriptionField.getText();
        double newPrice = Double.parseDouble(priceField.getText());
        String newImageUrl = imageUrlField.getText();
        String specialLabel = specialLabelField.getText();

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

        Product updatedProduct = new Product(
            product.getId(), newName, newDescription, newPrice, newImageUrl, specialLabel, true, selectedTags
        );

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
        new Label("Image URL:"), imageBox,
        new Label("Special Label:"), specialLabelField,
        tagsLabelBox,
        tagsBox,
        saveButton
    );

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

  public void createProduct(ActionEvent actionEvent) {
    Stage dialog = new Stage();
    dialog.initModality(Modality.APPLICATION_MODAL);
    dialog.setTitle("Create Product");

    VBox vbox = new VBox(10);
    vbox.setPadding(new Insets(20));

    // Empty fields for new product
    TextField nameField = new TextField();
    TextField descriptionField = new TextField();
    TextField priceField = new TextField();
    TextField specialLabelField = new TextField();
    HBox imageBox = new HBox(5);

    TextField imageUrlField = new TextField("");

    imageUrlField.setPrefWidth(250);

    Button browseButton = new Button("Browse");
    browseButton.setOnAction(e -> {
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Choose Image File");
      fileChooser.getExtensionFilters().addAll(
              new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
      );
      File selectedFile = fileChooser.showOpenDialog(dialog);
      if (selectedFile != null) {
        imageUrlField.setText(selectedFile.toURI().toString());
      }
    });

    StackPane dragDropArea = new StackPane();
    dragDropArea.setPrefSize(200, 100);
    dragDropArea.setStyle("-fx-border-color: gray; -fx-border-style: dashed; -fx-border-width: 2; -fx-background-color: #f0f0f0;");
    Label dragDropLabel = new Label("Drag & Drop Image Here");
    dragDropArea.getChildren().add(dragDropLabel);

    dragDropArea.setOnDragOver(event -> {
      if (event.getGestureSource() != dragDropArea &&
              event.getDragboard().hasFiles()) {
        event.acceptTransferModes(javafx.scene.input.TransferMode.COPY_OR_MOVE);
      }
      event.consume();
    });

    dragDropArea.setOnDragDropped(event -> {
      var db = event.getDragboard();
      boolean success = false;
      if (db.hasFiles()) {
        File file = db.getFiles().get(0);
        if (file != null && isImageFile(file)) {
          imageUrlField.setText(file.toURI().toString());
          success = true;
        }
      }
      event.setDropCompleted(success);
      event.consume();
    });

    imageBox.getChildren().addAll(imageUrlField, browseButton, dragDropArea);

    browseButton.setOnAction(e -> {
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Choose Image File");
      fileChooser.getExtensionFilters().addAll(
              new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
      );
      File selectedFile = fileChooser.showOpenDialog(dialog);
      if (selectedFile != null) {
        imageUrlField.setText(selectedFile.toURI().toString());
      }
    });


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
      tagCheckboxes.add(checkBox);
      tagsBox.getChildren().add(checkBox);
    }

    // HBox for Tags Label + Add Button
    HBox tagsLabelBox = new HBox(5);
    Label tagsLabel = new Label("Tags:");
    Button addTagButton = new Button("+");
    addTagButton.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-background-radius: 5;");
    tagsLabelBox.getChildren().addAll(tagsLabel, addTagButton);

    addTagButton.setOnAction(e -> {
      TextInputDialog inputDialog = new TextInputDialog();
      inputDialog.setTitle("Add New Tag");
      inputDialog.setHeaderText(null);
      inputDialog.setContentText("Enter new tag name:");

      inputDialog.showAndWait().ifPresent(tagName -> {
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
    saveButton.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-background-radius: 5;");

    saveButton.setOnAction(e -> {
      try {
        String name = nameField.getText();
        String description = descriptionField.getText();
        double price = Double.parseDouble(priceField.getText());
        String imageUrl = imageUrlField.getText();
        String specialLabel = specialLabelField.getText();

        List<Tag> selectedTags = new ArrayList<>();
        for (int i = 0; i < tagCheckboxes.size(); i++) {
          if (tagCheckboxes.get(i).isSelected()) {
            selectedTags.add(allTags.get(i));
          }
        }

        Product newProduct = new Product(
                0, // or null, depending on how IDs are generated
                name,
                description,
                price,
                imageUrl,
                specialLabel,
                true,
                selectedTags

        );

        repository.save(newProduct); // make sure you have this method
        loadProducts(); // refresh list
        dialog.close();

      } catch (NumberFormatException ex) {
        showAlert("Invalid price format.");
      } catch (IllegalArgumentException ex) {
        showAlert(ex.getMessage());
      } catch (SQLException ex) {
        showAlert("Error saving product: " + ex.getMessage());
      }
    });

    vbox.getChildren().addAll(
            new Label("Name:"), nameField,
            new Label("Description:"), descriptionField,
            new Label("Price:"), priceField,
            new Label("Image URL:"), imageBox,
            new Label("Special Label:"), specialLabelField,
            tagsLabelBox,
            tagsBox,
            saveButton
    );

    Scene scene = new Scene(vbox, 400, 600);
    dialog.setScene(scene);
    dialog.showAndWait();
  }

  public void goToLanguagesPage(ActionEvent actionEvent) {
    sceneRouter.goToLanguagesPage();
  }
}
