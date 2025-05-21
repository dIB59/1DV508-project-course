package org.example.features.ingredients;

import org.example.database.Identifiable;

public class Ingredient implements Identifiable<Integer> {
    private int id;
    private String name;
    private double price;

    public Ingredient(int id, String name, double price) {
        if(name == null || name.isBlank()){
            throw new IllegalArgumentException("Name cannot be null or empty");
        }

        if(price < 0 ) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public void setID(int id){
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name){
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
    this.name = name;
    }

    public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    if (price < 0) {
      throw new IllegalArgumentException("Price cannot be negative");
    }
    this.price = price;
  }


}
