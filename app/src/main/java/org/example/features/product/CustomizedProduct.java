package org.example.features.product;

import java.util.Map;

public class CustomizedProduct {
  private Product product;
  private Map<String, Integer> ingredientCounts;


  public CustomizedProduct(Product product, Map<String, Integer> ingredientCounts) {
    this.product = product;
    this.ingredientCounts = ingredientCounts;
  }

  public Product getProduct() {
    return product;
  }

  public Map<String, Integer> getIngredientCounts() {
    return ingredientCounts;
  }

}
