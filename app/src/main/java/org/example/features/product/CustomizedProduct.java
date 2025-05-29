package org.example.features.product;

import java.util.Map;
import java.util.Objects;
import org.example.features.ingredients.Ingredient;

public class CustomizedProduct {
  private final Product product;
  private final Map<Ingredient, Integer> ingredientQuantities;

  public CustomizedProduct(Product product, Map<Ingredient, Integer> ingredientQuantities) {
    this.product = product;
    this.ingredientQuantities = ingredientQuantities;
  }

  public Product getProduct() {
    return product;
  }

  public Map<Ingredient, Integer> getIngredientquanities() {
    return ingredientQuantities;
  }

  public double getTotalPrice() {
    double basePrice = product.getPrice();
    double ingprice = 0.0;

    Map<Ingredient, Integer> defaultIngredients = product.getIngredients();

    for (Map.Entry<Ingredient, Integer> entry : ingredientQuantities.entrySet()) {
      Ingredient ingredient = entry.getKey();
      int quantity = entry.getValue();
      int defaultQty = defaultIngredients.getOrDefault(ingredient, 0);

      int extraQty = quantity - defaultQty;

      if (extraQty > 0) {
        ingprice += ingredient.getPrice() * extraQty;
      }
    }

    return basePrice + ingprice;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    CustomizedProduct other = (CustomizedProduct) obj;
    return Objects.equals(product.getId(), other.product.getId()) &&
        Objects.equals(ingredientQuantities, other.ingredientQuantities);
  }
}
