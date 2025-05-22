package org.example.features.help;

import java.util.concurrent.atomic.AtomicInteger;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class HelpController {
  @FXML
  private Label helpLabel;

  public void initialize(){
    makeTextBlink(helpLabel);
  }

  public static void makeTextBlink(Label helpLabel) {
    AtomicInteger size = new AtomicInteger(70);  // start size at 50

    Timeline timeline = new Timeline(
        new KeyFrame(Duration.seconds(2), e -> {
          size.getAndIncrement();  // increase size by 1

          String currentStyle = helpLabel.getStyle();

          if (currentStyle.contains("red")) {
            helpLabel.setStyle("-fx-text-fill: white; -fx-font-size: " + size.get() + "px; -fx-font-weight: bold;");
          } else {
            helpLabel.setStyle("-fx-text-fill: red; -fx-font-size: " + size.get() + "px; -fx-font-weight: bold;");
          }
        })
    );
    timeline.setCycleCount(Timeline.INDEFINITE);
    timeline.play();
  }
}
