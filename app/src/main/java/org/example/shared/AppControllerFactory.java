package org.example.shared;

import java.lang.reflect.InvocationTargetException;
import javafx.util.Callback;
import org.example.features.checkout.CheckoutController;
import org.example.features.home.HomeController;
import org.example.features.home.HomeModel;
import org.example.features.menu.MenuController;
import org.example.features.menu.MenuModel;
import org.example.features.order.OrderService;

public class AppControllerFactory implements Callback<Class<?>, Object> {

  private final OrderService orderService;
  private final SceneRouter sceneRouter;

  public AppControllerFactory(OrderService orderService, SceneRouter sceneRouter) {
    this.orderService = orderService;
    this.sceneRouter = sceneRouter;
  }

  @Override
  public Object call(Class<?> controllerClass) {
    return switch (controllerClass.getSimpleName()) {
      case "HomeController" -> new HomeController(new HomeModel(), sceneRouter);
      case "MenuController" -> new MenuController(new MenuModel(), sceneRouter, orderService);
      case "CheckoutController" -> new CheckoutController(orderService, sceneRouter);
      default -> {
        try {
          yield controllerClass
              .getDeclaredConstructor(OrderService.class, SceneRouter.class)
              .newInstance(orderService, sceneRouter);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException
                 | InvocationTargetException e) {
          throw new RuntimeException(
              "Failed to create controller instance for " + controllerClass.getName(), e);
        }
      }
    };
  }


}

