package org.example;

import java.sql.Connection;
import javafx.application.Application;
import javafx.stage.Stage;
import org.example.database.CrudRepository;
import org.example.database.Database;
import org.example.features.order.Order;
import org.example.features.order.OrderMapper;
import org.example.features.order.OrderRepository;
import org.example.features.order.OrderService;
import org.example.shared.SceneRouter;

/** The type App. */
public class App extends Application {

  private final Database database;

  public App() {
    this.database = Database.getInstance();
  }

  @Override
  public void start(Stage primaryStage) {

    Connection conn = this.database.getConnection();
    var orderMapper = new OrderMapper();
    CrudRepository<Order, Integer> orderRepository = new OrderRepository(conn, orderMapper);
    primaryStage.setMaximized(true);
    var orderService = new OrderService(orderRepository);

    SceneRouter router = new SceneRouter(primaryStage, orderService);

    primaryStage.setTitle("JavaFX with MySQL");
    router.goTo(SceneRouter.KioskPage.HOME);
  }
}
