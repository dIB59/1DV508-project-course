package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.stage.Stage;
import org.example.shared.AppControllerFactory;
import org.example.features.order.OrderService;
import org.example.shared.SceneRouter;

public class App extends Application {

  private final OrderService orderService = new OrderService();
  
  @Override
  public void start(Stage primaryStage) {


    SceneRouter router = new SceneRouter(primaryStage, orderService);
    AppControllerFactory controllerFactory = new AppControllerFactory(orderService, router);
    router.setControllerFactory(controllerFactory);

    try {
      Connection conn = DriverManager.getConnection(
          "jdbc:mysql://localhost/"
              + "kioske?user=main&password=root&useSSL=false&allowPublicKeyRetrieval=true");

    } catch (SQLException e) {
      System.err.println("Connection failed: " + e.getMessage());
    }

    router.goTo(SceneRouter.KioskPage.HOME);
    primaryStage.setTitle("JavaFX with MySQL");
    primaryStage.show();

  }
}
