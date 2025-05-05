package org.example.features.product;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class IngredientController {

  private Map<String, Integer> ingredientCounts = new HashMap<>();
  private ArrayList<Map<String,Integer>> allingredientCounts = new ArrayList<>();

  public HBox createIngredientControl(String ingredientName) {

    // Initialize the VBox for this ingredient control
    HBox ingredientBox = new HBox(10);
    ingredientBox.setAlignment(Pos.CENTER);

    // Ingredient Name Label
    Label ingredientLabel = new Label(ingredientName);
    ingredientLabel.setStyle("-fx-font-size: 14px;");

    // Ingredient Count (display the count, start from 0)
    Label ingredientCount = new Label("0");
    ingredientCounts.put(ingredientName, 0);  // Initialize count to 0 in the map

    // Plus Button (increment count)
    Button plusButton = new Button("+");
    plusButton.setOnAction(event -> {
      int count = ingredientCounts.get(ingredientName); // Get the current count
      count += 1; // Increment the count
      ingredientCounts.put(ingredientName, count); // Update the map with new count
      ingredientCount.setText(String.valueOf(count)); // Update the label
    });

    // Minus Button (decrement count)
    Button minusButton = new Button("-");
    minusButton.setOnAction(event -> {
      int count = ingredientCounts.get(ingredientName); // Get the current count
      if (count > 0) {
        count -= 1; // Decrement the count
        ingredientCounts.put(ingredientName, count); // Update the map
        ingredientCount.setText(String.valueOf(count)); // Update the label
      }
    });

    // Add all elements (Label, count, buttons) to the VBox
    ingredientBox.getChildren().addAll(ingredientLabel, ingredientCount, plusButton, minusButton);
    return ingredientBox;
  }

  public Map<String, Integer> getIngredientCounts() {
    return ingredientCounts;
  }

  public void clearIngredientCounts() {
    ingredientCounts.clear();
  }


}
