package org.example;

import java.io.Serializable;

public class Shelf implements Serializable {

    private int shelfNumber;
    private MLinkedList<GoodItem> goodItems;

    public Shelf(int shelfNumber) {
        this.shelfNumber = shelfNumber;
        this.goodItems = new MLinkedList<>();
    }

    /// TO DO
    /// ADD GETTERS , SETTERS , ETC

    //   --
    //   GETTERS
    //   --
    public int getShelfNumber() {
        return shelfNumber;
    }

    public MLinkedList<GoodItem> getGoodItems() {
        return goodItems;
    }

    //   --
    //   SETTERS
    //   --
    public void setShelfNumber(int shelfNumber) {
        this.shelfNumber = shelfNumber;
    }

    public void setGoodItems(MLinkedList<GoodItem> goodItems) {
        this.goodItems = goodItems;
    }

    public double getTotalValue() {
        double total = 0.0;
        for (int i = 0; i < goodItems.size(); i++) {
            total += goodItems.get(i).getTotalValue();
        }
        return total;
    }

    public void addGoodItem(GoodItem newItem) {
        for (int i = 0; i < goodItems.size(); i++) {
            GoodItem existing = goodItems.get(i);
            if (existing.matches(newItem)) {
                existing.setQuantity(existing.getQuantity() + newItem.getQuantity());
                existing.setUnitPrice(newItem.getUnitPrice());
                existing.setPhotoUrl(newItem.getPhotoUrl());
                return;
            }
        }
        goodItems.addElement(newItem);
    }

    public void removeGoodItem(String description, int quantity) {
        for (int i = 0; i < goodItems.size(); i++) {
            GoodItem item = goodItems.get(i);
            if (item.getDescription().equalsIgnoreCase(description)) {
                if (quantity >= item.getQuantity()) {
                    goodItems.remove(item);
                } else {
                    item.setQuantity(item.getQuantity() - quantity);
                }
                return;
            }
        }
    }

}
