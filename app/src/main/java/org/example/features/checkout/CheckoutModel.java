package org.example.features.checkout;

import java.util.ArrayList;
import java.util.List;
import org.example.Item;

public class CheckoutModel {

  private List<Item> items;
  private static CheckoutModel instance;

  private CheckoutModel() {
    this.items = new ArrayList<>();
  }


  public static CheckoutModel getInstance() {
    if (instance == null) {
      instance = new CheckoutModel();
    }
    return instance;
  }

  public List<Item> getItems() {
    return items;
  }

  public void add(Item item) {
    items.add(item);
  }

  public void addAll(List<Item> item) {
    items.addAll(item);
  }

  public void remove(Item item) {
    items.remove(item);
  }


}
