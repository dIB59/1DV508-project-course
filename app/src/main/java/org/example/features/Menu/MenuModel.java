package org.example.features.Menu;

import org.example.Item;
import java.util.ArrayList;
import java.util.List;

public class MenuModel {

    private List<Item> menuItems;

    public MenuModel() {
        this.menuItems = new ArrayList<>();
    }

    public String getMenuTitle() {
        return "This is the Menu page";
    }

    public List<Item> getItems() {
        return menuItems;
    }

    public void add(Item item) {
        menuItems.add(item);
    }   

    public void addAll(List<Item> items) {
        menuItems.addAll(items);
    }

    public void remove(Item item) {
        menuItems.remove(item);
    }

}
