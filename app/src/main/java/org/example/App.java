package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.router.SceneRouter;

public class App extends Application {
  
  @Override
  public void start(Stage primaryStage) {
    VBox root = new VBox();
    root.setPadding(new Insets(5));
    Label title = new Label("JavaFX");
    Button goToHomeButton = new Button("Go to Home");

    goToHomeButton.setOnAction(event -> SceneRouter.goToHomePage());

    try {
      Connection conn = DriverManager.getConnection(
          "jdbc:mysql://localhost/"
              + "kioske?user=main&password=root&useSSL=false&allowPublicKeyRetrieval=true");

    } catch (SQLException e) {
      System.err.println("Connection failed: " + e.getMessage());
    }

    root.getChildren().addAll(title);

    SceneRouter.setStage(primaryStage);
    SceneRouter.goTo(SceneRouter.KioskPage.HOME);

    primaryStage.setTitle("JavaFX with MySQL");
    root.getChildren().add(goToHomeButton);
    Scene scene = new Scene(root, 300, 250);

    primaryStage.setScene(scene);

    primaryStage.show();

  }
}
