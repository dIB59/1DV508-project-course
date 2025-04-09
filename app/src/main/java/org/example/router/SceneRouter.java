package org.example.router;

import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneRouter {
  private static Stage stage;
  private static KioskPage currentPage;

  public static void setStage(Stage primaryStage) {
    stage = primaryStage;
  }

  public static void goTo(KioskPage screen) {
    try {
      URL url = SceneRouter.class.getResource("/" + screen.getValue());
      System.out.println("Loading scene from: " + url);
      FXMLLoader loader = new FXMLLoader(url);
      currentPage = screen;
      Scene scene = new Scene(loader.load());
      stage.setScene(scene);
      stage.show();
    } catch (IOException e) {
      System.err.println("Error loading scene: " + e.getMessage());
    }
  }

  public static void refreshPage() {
    stage.show();
  }

  public static KioskPage getCurrentPage() {
    return currentPage;
  }

  public static void goToHomePage() {
    goTo(KioskPage.HOME);
  }

  public static void goToOrderPage() {
    goTo(KioskPage.ORDER);
  }

  public static void goToDashboardPage() {
    goTo(KioskPage.DASHBOARD);
  }

  public static void goToCheckoutPage() {
    goTo(KioskPage.CHECKOUT);
  }


  public enum KioskPage {
    HOME("HomeView.fxml"),
    ORDER("OrderView.fxml"),
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
