package org.example.features.product;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;

public class IngredientController {

  @FXML
  private VBox ingredientsBox; // Vbox in a vbox holding all ingredients

  private Map<String, Integer> ingredientCount = new HashMap<>(); // Maps Str, Int of items created in a hashmap

  private VBox createIngredients(String ingredientName){
    Label nameLabel = new Label(ingredientName + ":");
    Button minusButton = new Button("-");
    Label countLabel = new Label("0");
    Button plusButton = new Button("+");

    ingredientCount.put(ingredientName, 0);

  }
}
