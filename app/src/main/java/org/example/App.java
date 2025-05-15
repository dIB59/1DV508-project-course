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
  private final AppContext appContext;

  public App() {
    this.database = Database.getInstance();
    this.appContext = AppContext.getInstance();
  }

  @Override
  public void start(Stage primaryStage) {
    System.out.println(AppConfig.getMerchantId());
    System.out.println(AppConfig.getPayPalPublicKey());
    System.out.println(AppConfig.getPayPalPrivateKey());


    Connection conn = this.database.getConnection();
    var orderMapper = new OrderMapper();
    CrudRepository<Order, Integer> orderRepository = new OrderRepository(conn, orderMapper);
    primaryStage.setMaximized(true);
    var orderService = new OrderService(orderRepository);

    SceneRouter router = new SceneRouter(primaryStage, orderService);

    primaryStage.setTitle("JavaFX with MySQL");
    router.goTo(SceneRouter.KioskPage.HOME);
    primaryStage.setMaximized(true);
    primaryStage.show();
  }
}
