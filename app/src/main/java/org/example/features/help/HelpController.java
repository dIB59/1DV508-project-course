package org.example.features.help;

import java.util.concurrent.atomic.AtomicInteger;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;
import javafx.animation.PauseTransition;
import org.example.shared.SceneRouter;


public class HelpController {

  private PauseTransition pauseTransition;
  private SceneRouter sceneRouter;

  public HelpController(SceneRouter sceneRouter) {
    this.sceneRouter = sceneRouter;
  }

  @FXML
  private Label helpLabel;

  public void initialize(){
    makeTextBlink(helpLabel);
    StartTimer();
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

  public void goToHomePage(){
    if (pauseTransition != null) {
      pauseTransition.stop(); // Stop the timer if still running
    }
    sceneRouter.goToHomePage();
  }

  private void StartTimer(){
    pauseTransition = new PauseTransition(Duration.seconds(10));
    pauseTransition.setOnFinished(e -> {goToHomePage();});
    pauseTransition.play();
  }
}
