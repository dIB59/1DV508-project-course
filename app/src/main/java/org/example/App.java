package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.geometry.Insets;
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
    Label mysql;

    try {
      Connection conn = DriverManager.getConnection(
          "jdbc:mysql://localhost/"
              + "kioske?user=main&password=root&useSSL=false&allowPublicKeyRetrieval=true");
      mysql = new Label("Driver found and connected");

    } catch (SQLException e) {
      mysql = new Label("An error has occurred: " + e.getMessage());
    }

    root.getChildren().addAll(title, mysql);

    SceneRouter.setStage(primaryStage);
    SceneRouter.goTo(SceneRouter.KioskPage.HOME);

    primaryStage.setTitle("JavaFX with MySQL");
    primaryStage.setScene(new javafx.scene.Scene(root, 300, 250));
    primaryStage.show();

  }
}
