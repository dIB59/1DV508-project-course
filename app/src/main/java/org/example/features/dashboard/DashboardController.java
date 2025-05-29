package org.example.features.dashboard;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional; // Import Optional
import java.util.function.Consumer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
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
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.features.ingredients.Ingredient;
import org.example.features.ingredients.IngredientsRepository;
import org.example.features.product.Product;
import org.example.features.product.ProductRepository;
import org.example.features.product.Tag;
import org.example.shared.SceneRouter;

public class DashboardController {

  private final SceneRouter sceneRouter;
  private final ProductRepository repository;
  private final IngredientsRepository ingredientsRepository;
  @FXML private VBox productList; // The VBox inside the ScrollPane

  public DashboardController(
      SceneRouter sceneRouter,
      ProductRepository repository,
      IngredientsRepository ingredientsRepository) {
    this.sceneRouter = sceneRouter;
    this.repository = repository;
    this.ingredientsRepository = ingredientsRepository;
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

  public void goToSettingsPage() {
    sceneRouter.goToSettingsPage();
  }
  private boolean isImageFile(File file) {
    String fileName = file.getName().toLowerCase();
    return fileName.endsWith(".png")
        || fileName.endsWith(".jpg")
        || fileName.endsWith(".jpeg")
        || fileName.endsWith(".gif");
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
    // Call the specific editProduct method
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
    deleteButton.setOnAction(
        e -> {
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

  /**
   * Displays a dialog for creating or editing a product. This method is now truly generic and does
   * not contain logic to decide between create or update.
   *
   * @param initialProductData The product data to pre-fill the fields with. Can be null for empty
   *     fields.
   * @param saveOrUpdateAction The action to perform when the save button is clicked. This Consumer
   *     takes the final Product object constructed from dialog inputs.
   * @param dialogTitle The title of the dialog window.
   */
  private void showProductDialog(
      Optional<Product> initialProductData,
      Consumer<Product> saveOrUpdateAction,
      String dialogTitle) {
    Stage dialog = createAndConfigureDialog(dialogTitle);

    // UI Elements
    TextField nameField = new TextField(initialProductData.map(Product::getName).orElse(""));
    TextField descriptionField =
        new TextField(initialProductData.map(Product::getDescription).orElse(""));
    TextField priceField =
        new TextField(initialProductData.map(p -> String.valueOf(p.getPrice())).orElse(""));
    TextField specialLabelField =
        new TextField(initialProductData.map(Product::getSpecialLabel).orElse(""));
    TextField imageUrlField =
        new TextField(initialProductData.map(Product::getImageUrl).orElse(""));

    imageUrlField.setPrefWidth(250);

    // Image section
    HBox imageSection = createImageSection(dialog, imageUrlField);

    // Tags and Ingredients
    List<Tag> allTags = repository.findAllTags();
    List<Ingredient> allIngredients = ingredientsRepository.findAll();

    // Tag checkboxes
    VBox tagsBox = new VBox(5);
    List<CheckBox> tagCheckboxes = createTagCheckboxes(allTags, initialProductData, tagsBox);
    ScrollPane tagsScroll = new ScrollPane(tagsBox);
    tagsScroll.setFitToWidth(true);
    tagsScroll.setPrefHeight(150);
    tagsScroll.setMaxWidth(Double.MAX_VALUE);

    // Add tag button
    Button addTagButton = createTagButton(allTags, tagCheckboxes, tagsBox);

    VBox tagsColumn = new VBox(5, styledLabel("Tags:"), tagsScroll, addTagButton);
    HBox.setHgrow(tagsColumn, Priority.ALWAYS);
    tagsColumn.setMaxWidth(Double.MAX_VALUE);

    // Ingredient checkboxes
    VBox ingredientsBox = new VBox(5);
    List<CheckBox> ingredientCheckboxes =
        createIngredientCheckboxes(allIngredients, initialProductData, ingredientsBox);
    ScrollPane ingredientsScroll = new ScrollPane(ingredientsBox);
    ingredientsScroll.setFitToWidth(true);
    ingredientsScroll.setPrefHeight(150);
    ingredientsScroll.setMaxWidth(Double.MAX_VALUE);

    // Add ingredient button
    Button addIngredientButton =
        createIngredientButton(allIngredients, ingredientCheckboxes, ingredientsBox);

    VBox ingredientsColumn =
        new VBox(5, styledLabel("Ingredients:"), ingredientsScroll, addIngredientButton);
    HBox.setHgrow(ingredientsColumn, Priority.ALWAYS);
    ingredientsColumn.setMaxWidth(Double.MAX_VALUE);

    // Combine into side-by-side columns
    HBox tagsAndIngredientsBox = new HBox(20, tagsColumn, ingredientsColumn);
    tagsAndIngredientsBox.setPadding(new Insets(10, 0, 10, 0));
    tagsAndIngredientsBox.setPrefWidth(Double.MAX_VALUE);
    tagsAndIngredientsBox.setMaxWidth(Double.MAX_VALUE);

    // Save button
    Button saveButton = createSaveButton();
    saveButton.setOnAction(
        e ->
            handleSaveAction(
                dialog,
                initialProductData,
                saveOrUpdateAction,
                nameField,
                descriptionField,
                priceField,
                imageUrlField,
                specialLabelField,
                allTags,
                tagCheckboxes,
                allIngredients,
                ingredientCheckboxes));

    // Layout
    VBox vbox = new VBox(10);
    vbox.setPadding(new Insets(20));
    vbox.getChildren()
        .addAll(
            new Label("Name:"),
            nameField,
            new Label("Description:"),
            descriptionField,
            new Label("Price:"),
            priceField,
            new Label("Image URL:"),
            imageSection,
            new Label("Special Label:"),
            specialLabelField,
            tagsAndIngredientsBox,
            saveButton);

    Scene scene = new Scene(vbox, 500, 650);
    dialog.setScene(scene);
    dialog.showAndWait();
  }

  private Button createIngredientButton(
      List<Ingredient> allIngredients, List<CheckBox> ingredientCheckboxes, VBox ingredientsBox) {
    Button addButton = new Button("+");
    addButton.setStyle(
        "-fx-background-color: black; -fx-text-fill: white; -fx-background-radius: 5;");
    addButton.setOnAction(
        e -> showAddIngredientPopup(allIngredients, ingredientCheckboxes, ingredientsBox));
    return addButton;
  }

  private void showAddIngredientPopup(
      List<Ingredient> allIngredients, List<CheckBox> ingredientCheckboxes, VBox ingredientsBox) {
    Stage popup = new Stage();
    popup.initModality(Modality.APPLICATION_MODAL);
    popup.setTitle("Add Ingredient");
    popup.setResizable(false);

    // Fields
    Label titleLabel = new Label("Add New Ingredient");
    titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

    TextField nameField = new TextField();
    nameField.setPromptText("Ingredient name");
    nameField.setMaxWidth(Double.MAX_VALUE);

    TextField priceField = new TextField();
    priceField.setPromptText("Price (e.g., 3.50)");
    priceField.setMaxWidth(Double.MAX_VALUE);

    Button saveButton = new Button("Add");
    saveButton.setStyle(
        "-fx-background-color: #007BFF; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");
    saveButton.setMaxWidth(Double.MAX_VALUE);

    saveButton.setOnAction(
        e -> {
          String name = nameField.getText().trim();
          String priceStr = priceField.getText().trim();

          if (name.isEmpty() || priceStr.isEmpty()) {
            showAlert("Please enter both name and price.");
            return;
          }

          try {
            double price = Double.parseDouble(priceStr);
            if (price < 0) {
              showAlert("Price must be non-negative.");
              return;
            }

            int newIngredientId;
            try {
              newIngredientId = ingredientsRepository.save(new Ingredient(name, price)).getId();
            } catch (SQLException ex) {
              showAlert("Error saving ingredient: " + ex.getMessage());
              return;
            }

            Ingredient newIngredient = new Ingredient(newIngredientId, name, price);
            allIngredients.add(newIngredient);

            CheckBox newCheckBox = new CheckBox(newIngredient.getName());
            newCheckBox.setSelected(true);
            ingredientCheckboxes.add(newCheckBox);
            ingredientsBox.getChildren().add(newCheckBox);

            popup.close();
          } catch (NumberFormatException ex) {
            showAlert("Invalid price format.");
          }
        });

    VBox layout = new VBox(15, titleLabel, nameField, priceField, saveButton);
    layout.setPadding(new Insets(25));
    layout.setAlignment(Pos.CENTER);
    layout.setStyle(
        "-fx-background-color: #f9f9f9; -fx-border-radius: 10; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10, 0.3, 0, 4);");

    Scene scene = new Scene(layout, 320, 250);
    popup.setScene(scene);
    popup.showAndWait();
  }

  private Button createTagButton(List<Tag> allTags, List<CheckBox> tagCheckboxes, VBox tagsBox) {
    Button addTagButton = new Button("+");
    addTagButton.setStyle(
        "-fx-background-color: black; -fx-text-fill: white; -fx-background-radius: 5;");
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
    return addTagButton;
  }

  private Label styledLabel(String text) {
    Label label = new Label(text);
    label.setStyle(
        """
        -fx-font-size: 14px;
        -fx-text-fill: #333;
        -fx-font-weight: bold;
    """);
    return label;
  }

  private Stage createAndConfigureDialog(String title) {
    Stage dialog = new Stage();
    dialog.initModality(Modality.APPLICATION_MODAL);
    dialog.setTitle(title);
    return dialog;
  }

  private HBox createImageSection(Stage dialog, TextField imageUrlField) {
    HBox imageBox = new HBox(5);
    Button browseButton = getFileBrowseButton(dialog, imageUrlField); // Assuming this method exists
    StackPane dragDropArea = createDragDropArea(imageUrlField);
    imageBox.getChildren().addAll(imageUrlField, browseButton, dragDropArea);
    return imageBox;
  }

  private StackPane createDragDropArea(TextField imageUrlField) {
    StackPane dragDropArea = new StackPane();
    dragDropArea.setPrefSize(200, 100);
    dragDropArea.setStyle(
        "-fx-border-color: gray; -fx-border-style: dashed; -fx-border-width: 2; -fx-background-color: #f0f0f0;");
    Label dragDropLabel = new Label("Drag & Drop Image Here");
    dragDropArea.getChildren().add(dragDropLabel);

    dragDropArea.setOnDragOver(
        event -> {
          if (event.getGestureSource() != dragDropArea && event.getDragboard().hasFiles()) {
            event.acceptTransferModes(javafx.scene.input.TransferMode.COPY_OR_MOVE);
          }
          event.consume();
        });

    dragDropArea.setOnDragDropped(
        event -> {
          var db = event.getDragboard();
          boolean success = false;
          if (db.hasFiles()) {
            File file = db.getFiles().getFirst();
            if (file != null && isImageFile(file)) { // Assuming isImageFile method exists
              imageUrlField.setText(file.toURI().toString());
              success = true;
            }
          }
          event.setDropCompleted(success);
          event.consume();
        });
    return dragDropArea;
  }

  private List<CheckBox> createIngredientCheckboxes(
      List<Ingredient> allIngredients, Optional<Product> initialProductData, VBox tagsBox) {
    List<CheckBox> ingredientCheckboxes = new ArrayList<>();
    for (Ingredient ingredient : allIngredients) {
      CheckBox checkBox = new CheckBox(ingredient.getName());
      initialProductData.ifPresent(
          product -> {
            if (product.getIngredients().containsKey(ingredient)) {
              checkBox.setSelected(true);
            }
          });
      ingredientCheckboxes.add(checkBox);
      tagsBox.getChildren().add(checkBox);
    }
    return ingredientCheckboxes;
  }

  private List<CheckBox> createTagCheckboxes(
      List<Tag> allTags, Optional<Product> initialProductData, VBox tagsBox) {
    List<CheckBox> tagCheckboxes = new ArrayList<>();
    for (Tag tag : allTags) {
      CheckBox checkBox = new CheckBox(tag.getName());
      initialProductData.ifPresent(
          product -> {
            if (product.tagIds().contains(tag.getId())) {
              checkBox.setSelected(true);
            }
          });
      tagCheckboxes.add(checkBox);
      tagsBox.getChildren().add(checkBox);
    }
    return tagCheckboxes;
  }

  private HBox createTagsHeader(List<Tag> allTags, List<CheckBox> tagCheckboxes, VBox tagsBox) {
    HBox tagsLabelBox = new HBox(5);
    Label tagsLabel = new Label("Tags:");
    Button addTagButton = new Button("+");
    addTagButton.setStyle(
        "-fx-background-color: black; -fx-text-fill: white; -fx-background-radius: 5;");
    addTagButton.setOnAction(e -> handleAddTagAction(allTags, tagCheckboxes, tagsBox));
    tagsLabelBox.getChildren().addAll(tagsLabel, addTagButton);
    return tagsLabelBox;
  }

  private void handleAddTagAction(List<Tag> allTags, List<CheckBox> tagCheckboxes, VBox tagsBox) {
    TextInputDialog inputDialog = new TextInputDialog();
    inputDialog.setTitle("Add New Tag");
    inputDialog.setHeaderText(null);
    inputDialog.setContentText("Enter new tag name:");

    inputDialog
        .showAndWait()
        .ifPresent(
            tagName -> {
              if (!tagName.trim().isEmpty()) {
                try {
                  int newTagId =
                      repository.createTag(tagName.trim()); // Assuming 'repository' is accessible
                  Tag newTag = new Tag(newTagId, tagName.trim());
                  allTags.add(newTag);

                  CheckBox newCheckBox = new CheckBox(newTag.getName());
                  newCheckBox.setSelected(true);
                  tagCheckboxes.add(newCheckBox);
                  tagsBox.getChildren().add(newCheckBox);
                } catch (SQLException ex) {
                  showAlert(
                      "Error adding tag: " + ex.getMessage()); // Assuming showAlert method exists
                }
              }
            });
  }

  private Button createSaveButton() {
    Button saveButton = new Button("Save");
    saveButton.setStyle(
        "-fx-background-color: black; -fx-text-fill: white; -fx-background-radius: 5;");
    return saveButton;
  }

  private void handleSaveAction(
      Stage dialog,
      Optional<Product> initialProductData,
      Consumer<Product> saveOrUpdateAction,
      TextField nameField,
      TextField descriptionField,
      TextField priceField,
      TextField imageUrlField,
      TextField specialLabelField,
      List<Tag> allTags,
      List<CheckBox> tagCheckboxes,
      List<Ingredient> allIngredients,
      List<CheckBox> ingredientCheckboxes) {

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

      List<Ingredient> selectedIngredients = new ArrayList<>();
      for (CheckBox ingredientCheckbox : ingredientCheckboxes) {
        if (ingredientCheckbox.isSelected()) {
          selectedIngredients.add(
              allIngredients.get(ingredientCheckboxes.indexOf(ingredientCheckbox)));
        }
      }

      Product productToProcess =
          new Product(
              initialProductData.map(Product::getId).orElse(0),
              name,
              description,
              price,
              imageUrl,
              specialLabel,
              true,
              selectedTags,
              selectedIngredients);

      saveOrUpdateAction.accept(productToProcess);

      loadProducts(); // Assuming loadProducts method exists
      dialog.close();

    } catch (NumberFormatException ex) {
      showAlert("Invalid price format.");
    } catch (IllegalArgumentException ex) {
      showAlert(ex.getMessage());
    }
  }

  private Button getFileBrowseButton(Stage dialog, TextField imageUrlField) {
    Button browseButton = new Button("Browse");
    browseButton.setOnAction(
        e -> {
          FileChooser fileChooser = new FileChooser();
          fileChooser.setTitle("Choose Image File");
          fileChooser
              .getExtensionFilters()
              .addAll(
                  new FileChooser.ExtensionFilter(
                      "Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
          File selectedFile = fileChooser.showOpenDialog(dialog);
          if (selectedFile != null) {
            imageUrlField.setText(selectedFile.toURI().toString());
          }
        });
    return browseButton;
  }

  // --- Specific functions for Create and Edit ---

  private void editProduct(Product product) {
    // Pass the existing product data wrapped in Optional
    showProductDialog(
        Optional.of(product),
        p -> {
          try {
            repository.update(p);
          } catch (SQLException e) {
            // Wrap checked exception in a RuntimeException to satisfy Consumer interface
            throw new RuntimeException("Failed to update product: " + e.getMessage(), e);
          }
        },
        "Edit Product");
  }

  public void createProduct() {
    // Pass an empty Optional for initial product data
    showProductDialog(
        Optional.empty(),
        p -> {
          try {
            repository.save(p);
          } catch (SQLException e) {
            // Wrap checked exception in a RuntimeException to satisfy Consumer interface
            throw new RuntimeException("Failed to save product: " + e.getMessage(), e);
          }
        },
        "Create Product");
  }

  private void showAlert(String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Error");
    alert.setHeaderText(null);

    System.err.println("Error: " + message);
    alert.setContentText(message);
    alert.showAndWait();
  }

  public void goToLanguagesPage(ActionEvent actionEvent) {
    sceneRouter.goToLanguagesPage();
  }
}
