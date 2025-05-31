package org.example.shared;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import org.example.features.dashboard.RestaurantSettingsRepository;

public class BaseLayoutController {

  @FXML private HBox header;
  @FXML private StackPane contentPane;
  private final RestaurantSettingsRepository repository;
  private final SceneRouter sceneRouter;

  public BaseLayoutController(RestaurantSettingsRepository repository, SceneRouter sceneRouter) {
    this.repository = repository;
    this.sceneRouter = sceneRouter;
  }

  public void initialize() {
    header.getChildren().clear();
    addLogo();
  }

  private void addLogo() {
    var logo = repository.getLogo();
    ImageView logoView = new ImageView(logo);
    logoView.setFitHeight(70); // Optional: scale the logo
    logoView.setPreserveRatio(true);

    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);

    header.getChildren().addAll(logoView, spacer);
    logoView.setOnMouseClicked(e -> sceneRouter.goToHomePage());
  }

  public void setContent(Node content) {
    contentPane.getChildren().setAll(content);
  }
}
