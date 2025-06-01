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

  @FXML private StackPane contentPane;

  public void setContent(Node content) {
    contentPane.getChildren().setAll(content);
  }
}
