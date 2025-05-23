package org.example.shared;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

public class BaseLayoutController {

  @FXML private StackPane contentPane;

  public void setContent(Node content) {
    contentPane.getChildren().setAll(content);
  }
}
