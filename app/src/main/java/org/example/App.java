package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.checkout.CheckoutPage;
import org.example.checkout.CheckoutService;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        VBox root = new VBox();
        root.setPadding(new Insets(5));
        Label title = new Label("JavaFX");
        Label mysql;


        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/kioske?user=main&password=root&useSSL=false&allowPublicKeyRetrieval=true");
            mysql = new Label("Driver found and connected");

        } catch (SQLException e) {
            mysql = new Label("An error has occurred: " + e.getMessage());
        }

        root.getChildren().addAll(title, mysql);

        primaryStage.setScene(new Scene(root, 400, 200));
        primaryStage.setTitle("JavaFX");
        primaryStage.show();

        Button goToCheckout = new Button("Go to Checkout");

        goToCheckout.setOnAction(event -> {
            CheckoutService checkoutService = new CheckoutService();
            List<Item> items = new ArrayList<>();
            CheckoutPage checkoutPage = new CheckoutPage(checkoutService, items, primaryStage);
            checkoutPage.show();
        });

        root.getChildren().add(goToCheckout);


    }
}
