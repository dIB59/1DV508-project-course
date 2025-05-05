package org.example.shared;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import javafx.util.Callback;
import org.example.database.Database;
import org.example.features.admin.AdminController;
import org.example.features.admin.AdminMapper;
import org.example.features.admin.AdminRepository;
import org.example.features.checkout.CheckoutController;
import org.example.features.coupons.CouponsRepository;
import org.example.features.dashboard.DashboardController;
import org.example.features.dashboard.DashboardModel;
import org.example.features.home.HomeController;
import org.example.features.home.HomeModel;
import org.example.features.menu.MenuController;
import org.example.features.menu.MenuModel;
import org.example.features.order.OrderService;
import org.example.features.product.IngredientController;
import org.example.features.product.ProductDetailsController;
import org.example.features.product.ProductMapper;
import org.example.features.product.ProductRepository;
import org.example.features.receipt.ReceiptController;

/**
 * AppControllerFactory is a factory class that creates instances of controllers based on the
 * controller class name. It implements the Callback interface to provide a way to create controller
 * instances dynamically.
 */
public class AppControllerFactory implements Callback<Class<?>, Object> {

  private final OrderService orderService;
  private final IngredientController ingredientController = new IngredientController();
  private final SceneRouter sceneRouter;
  private final Connection connection = Database.getInstance().getConnection();

  /**
   * Instantiates a new App controller factory.
   *
   * @param orderService the order service
   * @param sceneRouter the scene router
   */
  public AppControllerFactory(OrderService orderService, SceneRouter sceneRouter) {
    this.orderService = orderService;
    this.sceneRouter = sceneRouter;
  }

  /**
   * Creates a controller instance based on the provided class. If you add a new controller, you
   * need to add a case for it in this method.
   *
   * @param controllerClass The class of the controller to be created.
   * @return An instance of the specified controller class.
   */
  @Override
  public Object call(Class<?> controllerClass) {
    return switch (controllerClass.getSimpleName()) {
      case "HomeController" -> new HomeController(new HomeModel(), sceneRouter);
      case "MenuController" ->
          new MenuController(new MenuModel(), getProductRepository(), sceneRouter, orderService);
      case "CheckoutController" -> new CheckoutController(orderService, getCouponsRepository(), sceneRouter);
      case "ProductDetailsController" -> new ProductDetailsController(orderService, sceneRouter, ingredientController);
      case "ReceiptController" ->
          new ReceiptController(orderService.saveOrderAndClear(), sceneRouter, orderService.getCustomizedProducts());
      case "AdminController" -> new AdminController(sceneRouter, getAdminRepository());
      case "DashboardController" ->
          new DashboardController(new DashboardModel(), sceneRouter, getProductRepository());
      case "IngredientController" -> ingredientController;
      default -> {
        try {
          yield controllerClass.getDeclaredConstructor(SceneRouter.class).newInstance(sceneRouter);
        } catch (NoSuchMethodException
            | InstantiationException
            | IllegalAccessException
            | InvocationTargetException e) {
          throw new RuntimeException(
              "Failed to create controller instance for " + controllerClass.getName(), e);
        }
      }
    };
  }

  private ProductRepository getProductRepository() {
    return new ProductRepository(connection, new ProductMapper());
  }

  private AdminRepository getAdminRepository() {
    return new AdminRepository(connection, new AdminMapper());
  }

  private CouponsRepository getCouponsRepository() {
    return new CouponsRepository(connection);
    }
}
