package org.example.features.menu;

public class MenuModel {

  private final Product[] menuItems;

  public MenuModel() {
    this.menuItems = new Product[] {
        new Product("Pizza", "Delicious cheese pizza", 9.99, "pizza.png"),
        new Product("Burger", "Juicy beef burger", 5.99, "burger.png"),
        new Product("Salad", "Fresh garden salad", 4.99, "salad.png"),
        new Product("Pasta", "Creamy Alfredo pasta", 7.99, "pasta.png"),
        new Product("Soda", "Refreshing soda", 1.99, "soda.png"),
        new Product("Ice Cream", "Creamy vanilla ice cream", 2.99, "ice_cream.png"),
        new Product("Fries", "Crispy French fries", 2.49, "fries.png"),
        new Product("Steak", "Grilled steak with sides", 19.99, "steak.png"),
        new Product("Tacos", "Spicy chicken tacos", 8.99, "tacos.png"),
    };
  }

  public Product[] getMenuItems() {
    return menuItems;
  }

  public void loadMenuItems() {
    System.out.println("Loading menu items...");
  }

}
