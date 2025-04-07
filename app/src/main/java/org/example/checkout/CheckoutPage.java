package org.example.checkout;

import java.awt.Button;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.example.Item;

public class CheckoutPage {

  private final CheckoutService checkoutService;
  private final List<Item> items;
  private Stage stage;

  public CheckoutPage(CheckoutService checkoutService, List<Item> items, Stage stage) {
    this.checkoutService = checkoutService;
    this.items = items;
    this.stage = stage;
  }

  public void show() {
    stage.setTitle("Checkout");
    Pane pane = new Pane();
    Button checkoutButton = new Button("Checkout");

    checkoutButton.addActionListener(event -> {
      checkoutService.printReceipt(items);
      stage.close();
    });
    stage.setScene(new Scene(pane, 300, 200));
    stage.show();
  }
  
  public void addCheckoutButton(Stage s) {
    javafx.scene.control.Button goToCheckout = new javafx.scene.control.Button("Go to Checkout");

    goToCheckout.setOnAction(event -> {
      CheckoutService checkoutService = new CheckoutService();
      ArrayList<Item> items = new ArrayList<>();
      CheckoutPage checkoutPage = new CheckoutPage(checkoutService, items, s);
      checkoutPage.show();
    });
  }
  
  

}
