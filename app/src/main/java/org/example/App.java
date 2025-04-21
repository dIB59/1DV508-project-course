package org.example;


import java.sql.Connection;
import javafx.application.Application;
import javafx.stage.Stage;
import org.example.features.order.Order;
import org.example.features.order.OrderMapper;
import org.example.features.order.OrderRepository;
import org.example.features.order.OrderService;
import org.example.shared.CrudRepository;
import org.example.shared.Database;
import org.example.shared.SceneRouter;

public class App extends Application {


  @Override
  public void start(Stage primaryStage) {

    Connection conn = Database.getInstance().getConnection();

    var orderMapper = new OrderMapper();
    CrudRepository<Order> orderRepository = new OrderRepository(conn, orderMapper);
    var orderService = new OrderService(orderRepository);

    SceneRouter router = new SceneRouter(primaryStage, orderService);

    router.goTo(SceneRouter.KioskPage.HOME);
    primaryStage.setTitle("JavaFX with MySQL");
    primaryStage.show();

  }
}
