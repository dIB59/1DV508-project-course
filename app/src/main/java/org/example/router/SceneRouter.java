package org.example.router;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import org.example.Item;
import org.example.features.checkout.CheckoutController;
import org.example.features.checkout.CheckoutModel;

public class SceneRouter {
  private static Stage stage;
  private static KioskPage currentPage;

  public static void setStage(Stage primaryStage) {
    stage = primaryStage;
  }

  public static void goTo(KioskPage screen) {
    if (stage == null) {
      throw new IllegalStateException("Stage is not set. Call setStage() first.");
    }

    try {
      URL url = SceneRouter.class.getResource("/" + screen.getValue());
      System.out.println("Loading scene from: " + url);
      FXMLLoader loader = new FXMLLoader(url);
      currentPage = screen;
      Scene scene = new Scene(loader.load());
      stage.setScene(scene);
      stage.show();
    } catch (IOException e) {
      System.err.println("Error loading scene: " + e.getLocalizedMessage());
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

  public static void goToMenuPage() {
    try {
      System.out.println("Menu page triggered");
      URL url = SceneRouter.class.getResource('/' + KioskPage.MENU.getValue());
      FXMLLoader loader = new FXMLLoader(url);
      
      currentPage = KioskPage.MENU;
      Scene scene = new Scene(loader.load());
      stage.setScene(scene);
      stage.show();
    } catch (IOException e) {
      System.err.println(e);
    }
  }

  public static void goToOrderPage() {
    goTo(KioskPage.ORDER);
  }

  public static void goToDashboardPage() {
    goTo(KioskPage.DASHBOARD);
  }

  public static void goToCheckoutPage(List<Item> items) {
    var checkoutModel = CheckoutModel.getInstance();

    try {
      URL url = SceneRouter.class.getResource("/" + KioskPage.CHECKOUT.getValue());
      FXMLLoader loader = new FXMLLoader(url);

      List<Item> mockItems = new ArrayList<>();
      for (int i = 0; i < 5; i++) {
        mockItems.add(new MockItem("" + i));
      }

      // Add items to the model
      checkoutModel.addAll(mockItems);
      checkoutModel.addAll(items);

      currentPage = KioskPage.CHECKOUT;
      Scene scene = new Scene(loader.load());
      stage.setScene(scene);
      stage.show();
    } catch (IOException e) {
      System.err.println(e.getLocalizedMessage());
    }
  }



  public enum KioskPage {
    HOME("HomeView.fxml"),
    MENU("MenuView.fxml"),
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

class MockItem implements Item {

  public MockItem(String id) {
    this.name = "Mock Item" + id;
    this.price = 10.0;
    this.quantity = 1;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public void setPrice(double price) {
    this.price = price;
  }

  private String name;
  private double price;
  private int quantity;


  @Override
  public String getName() {
    return name;
  }

  @Override
  public double getPrice() {
    return price;
  }

  @Override
  public int getQuantity() {
    return quantity;
  }

  @Override
  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  @Override
  public String toString() {
    return String.format("%s, Price: %.2f, Quantity: %d", getName(), getPrice(), getQuantity());
  }


}
