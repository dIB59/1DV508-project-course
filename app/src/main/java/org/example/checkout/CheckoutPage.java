package org.example.checkout;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.Item;

public class CheckoutPage {

  private final CheckoutService checkoutService;
  private final List<Item> items;
  private Stage stage;
  private VBox root;

  public CheckoutPage(CheckoutService checkoutService, List<Item> items, Stage stage, VBox root) {
    this.checkoutService = checkoutService;
    this.items = items;
    this.stage = stage;
    this.root = root;
  }

  public void show() {
    Pane pane = new Pane();
    Button checkoutButton = new Button("Checkout");

    checkoutButton.setOnAction(event -> {
      checkoutService.printReceipt(items);
      stage.setScene(root.getScene());
    });

    var checkoutStage = checkoutStage();
    checkoutStage.show();
  }

  private Stage checkoutStage() {
    Stage checkoutStage = new Stage();
    checkoutStage.setTitle("Checkout");
    Pane pane = new Pane();
    Button checkoutButton = new Button("Checkout");

    checkoutButton.setOnAction(event -> {
      checkoutService.printReceipt(items);
      stage.setScene(root.getScene());
    });

    pane.getChildren().add(checkoutButton);
    checkoutStage.setScene(new Scene(pane, 300, 200));
    return checkoutStage;
  }
  
  

}
