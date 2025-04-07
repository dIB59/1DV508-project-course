package org.example;

public interface Item {
    String getName();
    double getPrice();
    int getQuantity();
    void setQuantity(int quantity);
    void setPrice(double price);
    void setName(String name);
}
