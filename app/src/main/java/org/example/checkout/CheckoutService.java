package org.example.checkout;

import java.util.List;
import org.example.Item;

public class CheckoutService {

  public double calculateTotalPrice(List<Item> items) {
    double total = 0.0;
    for (Item item : items) {
      total += item.getPrice() * item.getQuantity();
    }
    return total;
  }

  public void printReceipt(List<Item> items) {
    System.out.println("Receipt:");
    for (Item item : items) {
      System.out.println(item.getName() + " x" + item.getQuantity() + ": $" + item.getPrice());
    }
    System.out.println("Total: $" + calculateTotalPrice(items));
  }


}
