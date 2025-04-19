package org.example.shared;

import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.features.order.OrderService;

public class SceneRouter {

  private final Stage stage;
  private final Callback<Class<?>, Object> controllerFactory;
  private KioskPage currentPage;

  public SceneRouter(Stage stage, OrderService orderService) {
    this.stage = stage;
    this.controllerFactory = new AppControllerFactory(orderService, this);
  }

  public void goTo(KioskPage page) {
    try {
      URL url = getClass().getResource("/" + page.getValue());
      FXMLLoader loader = new FXMLLoader(url);
      loader.setControllerFactory(controllerFactory);

      currentPage = page;
      Scene scene = new Scene(loader.load());
      stage.setScene(scene);
      stage.show();
    } catch (IOException e) {
      System.err.println("Failed to load scene: " + e.getLocalizedMessage());
      e.printStackTrace();
    }
  }

  public void refreshPage() {
    goTo(currentPage);
  }

  public KioskPage getCurrentPage() {
    return currentPage;
  }

  public void goToHomePage() {
    goTo(KioskPage.HOME);
  }

  public void goToMenuPage() {
    goTo(KioskPage.MENU);
  }

  public void goToDashboardPage() {
    goTo(KioskPage.DASHBOARD);
  }

  public void goToCheckoutPage() {
    goTo(KioskPage.CHECKOUT);
  }

  public void setControllerFactory(AppControllerFactory controllerFactory) {

  }

  public enum KioskPage {
    HOME("HomeView.fxml"),
    MENU("MenuView.fxml"),
    CHECKOUT("CheckoutView.fxml"),
    DASHBOARD("DashboardView.fxml");

    private final String value;

    KioskPage(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }
  }
}

